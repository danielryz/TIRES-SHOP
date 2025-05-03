import './Modal.css';

interface ModalProps {
    onClose: () => void;
    children: React.ReactNode;
}

const Modal = ({ onClose, children }: ModalProps) => {
    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button className="modal-close" onClick={onClose}>×</button>
                {children}
            </div>
        </div>
    );
};

export default Modal;
