package cz.cvut.fel.pjv.Controllers.ViewManagers;

import cz.cvut.fel.pjv.Controllers.GameLogicControllers.MoveEnder;
import cz.cvut.fel.pjv.Controllers.GameLogicControllers.MoveHandler;
import cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers.BoardMouseListener;
import cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers.MouseActionHandler;
import cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers.MouseListenerAll;
import cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers.PieceDeploymentManager;
import cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers.PieceDeploymentMouseListener;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IMessageBroker;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.ISubscriber;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Controllers.PieceCreationControllers.IPieceCreator;
import cz.cvut.fel.pjv.Controllers.PieceCreationControllers.RandomPieceCreator;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.AIPlayer;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.BasePlayer;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.IPlayer;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.Player;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.Models.Color;
import cz.cvut.fel.pjv.Models.StartGameInfo;
import cz.cvut.fel.pjv.Views.GameFrame;
import cz.cvut.fel.pjv.utils.*;

import javax.swing.*;
import java.util.Random;
import java.util.Timer;

/**
 * GameCreator creates classes, needed for the game and handles some events, when the game is over
 */
public class GameCreator implements ISubscriber
{
    private final IMessageBroker broker;
    private BoardModel board;
    private GameViewManager gameViewManager;
    private final IPublisher publisher;
    private IPieceCreator pieceCreator;
    private MouseActionHandler mouseActionHandler;
    private ITimer timerGold, timerSilver;
    private final Timer AITimer=new Timer();
    private BasePlayer player1, player2;
    private MoveEnder moveEnder;
    private GameRecorder gameRecorder;
    private IImageHandler imageHandler;
    public GameCreator(IMessageBroker broker, IPublisher publisher, IImageHandler imageHandler, StartGameInfo startGameInfo)
    {
        this.imageHandler = imageHandler;
        this.broker=broker;
        this.publisher=publisher;
        broker.addSubscriber(MessageType.ReturnToMenu, this);
        broker.addSubscriber(MessageType.BeginGame, this);
        broker.addSubscriber(MessageType.EndGame, this);
        createInitialSetup(startGameInfo);
    }

    private void createInitialSetup(StartGameInfo startGameInfo)
    {
        int baseTime= startGameInfo.baseTime();
        int timeAddition= startGameInfo.timeAddition();
        boolean againstAI=startGameInfo.againstAI();
        board = new BoardModel(publisher);
        createTimers(baseTime, timeAddition);
        createPlayers(againstAI);
        createPieces();
        setPieceDeploymentFaze();


    }

    private void createPieces()
    {

        pieceCreator = new RandomPieceCreator(board, publisher, imageHandler);
        pieceCreator.createPieceSet(Color.Silver);
        pieceCreator.createPieceSet(Color.Gold);
    }

    private void setPieceDeploymentFaze()
    {
        PieceDeploymentManager deploymentManager = new PieceDeploymentManager(board);
        MouseListenerAll mouseListener = new PieceDeploymentMouseListener(board, publisher);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameViewManager = new GameViewManager(board,mouseListener, publisher);
                broker.addSubscriber(MessageType.ChangePiece, gameViewManager);
                broker.addSubscriber(MessageType.GameStateNotification, gameViewManager);
                broker.addSubscriber(MessageType.DisableStartButton, gameViewManager);
            }
        });

        broker.addSubscriber(MessageType.ChangePiece,deploymentManager);

    }

    private void createTimers(int baseTime, int timeAddition)
    {
        if (baseTime<=0)
            return;
        timerGold = new ArimaaTimer(baseTime, timeAddition, Color.Gold,publisher);
        timerSilver = new ArimaaTimer(baseTime, timeAddition, Color.Silver,publisher);
    }
    private void createPlayers(boolean againstAI)
    {
        MoveHandler moveHandler = new MoveHandler(board, publisher);
        if (againstAI)
        {
            Random rand = new Random();
            MoveHandler AImoveHandler = new MoveHandler(board,null);
            if (rand.nextBoolean())
            {
                player1=new Player(Color.Gold, moveHandler, publisher, timerGold);
                player2 = new AIPlayer(Color.Silver, board, AImoveHandler, publisher, AITimer);
            }
            else
            {
                player1 = new AIPlayer(Color.Gold, board, AImoveHandler,publisher, AITimer);
                player2 = new Player(Color.Silver, moveHandler, publisher, timerSilver);
            }
        }
        else
        {
            player1 = new Player(Color.Gold, moveHandler, publisher, timerGold);
            player2 = new Player(Color.Silver, moveHandler, publisher, timerSilver);
        }
    }

    private void beginGame()
    {
        if (!pieceCreator.checkStartSets())
            return;
        setView();
        IPlayer currentPlayer = player1;
        this.moveEnder = new MoveEnder(board, player1,player2,currentPlayer,new MoveHandler(board, null), publisher);
        gameRecorder = new GameRecorder(board);
        broker.addSubscriber(MessageType.RecordMove, gameRecorder);
        broker.addSubscriber(MessageType.EndGame, gameRecorder);
        broker.addSubscriber(MessageType.TimerElapsed, moveEnder);

        broker.addSubscriber(MessageType.EndMove, gameRecorder);
        broker.addSubscriber(MessageType.EndMove,moveEnder);

        mouseActionHandler = new MouseActionHandler(board, moveEnder);
        broker.addSubscriber(MessageType.PieceMoved, mouseActionHandler);
        currentPlayer.startMove();
    }

    private void setView()
    {
        MouseListenerAll mouseListener = new BoardMouseListener(board, publisher);
        gameViewManager.setMouseListener(mouseListener);
        broker.addSubscriber(MessageType.FieldColorChanged, gameViewManager);
        broker.addSubscriber(MessageType.PiecePosChanged, gameViewManager);
        broker.addSubscriber(MessageType.PieceDragged, gameViewManager);
        broker.addSubscriber(MessageType.EndMove, gameViewManager);
        broker.addSubscriber(MessageType.TimerFired, gameViewManager);
        broker.addSubscriber(MessageType.EndGame, gameViewManager);
        broker.addSubscriber(MessageType.disableEndMoveButton, gameViewManager);

    }


    private void terminateGameInstance()
    {
        gameViewManager.closeWindow();
        cancelTimers();
    }

    private void cancelTimers()
    {
        if (timerGold instanceof ArimaaTimer)
            ((ArimaaTimer) timerGold).dispose();
        if (timerSilver instanceof ArimaaTimer)
            ((ArimaaTimer) timerSilver).dispose();
        AITimer.cancel();
    }

    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        switch (type)
        {
            case ReturnToMenu -> {
                if (data instanceof GameFrame)
                    terminateGameInstance();
            }
            case EndGame -> cancelTimers();
            case BeginGame -> beginGame();
        }

    }
}
