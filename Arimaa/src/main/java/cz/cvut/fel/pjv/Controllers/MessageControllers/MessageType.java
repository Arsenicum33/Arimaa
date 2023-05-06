package cz.cvut.fel.pjv.Controllers.MessageControllers;

public enum MessageType
{
    FieldColorChanged,
    PiecePosChanged,
    GameStateNotification,

    EndMove,

    PieceDragged,

    PieceMoved,

    ChangePiece,

    BeginGame,

    EndGame,

    TimerFired,

    TimerElapsed,

    CreateGame,

    ReturnToMenu,

    RecordMove,

    OpenRecords,

    PlayRecord,

    NextMoveOfRecord,

    ReturnToRecords,
    disableEndMoveButton,
    DisableStartButton

}
