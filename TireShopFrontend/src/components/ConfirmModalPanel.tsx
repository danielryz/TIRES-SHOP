import "./ConfirmModalPanel.css";

interface ConfirmModalProps {
  title?: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
}

const ConfirmModalPanel = ({
  title = "Potwierdzenie",
  message,
  onConfirm,
  onCancel,
}: ConfirmModalProps) => {
  return (
    <div className="confirm-panel-overlay" onClick={onCancel}>
      <div className="confirm-panel-box" onClick={(e) => e.stopPropagation()}>
        <h3>{title}</h3>
        <p>{message}</p>
        <div className="confirm-panel-actions">
          <button onClick={onConfirm} className="confirm-panel-yes">
            Tak
          </button>
          <button onClick={onCancel} className="confirm-panel-no">
            Anuluj
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModalPanel;
