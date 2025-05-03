import { useEffect, useState } from 'react';
import './Alert.css';

interface AlertProps {
    message: string;
    type: 'success' | 'error';
    onClose: () => void;
    duration?: number;
}

const Alert = ({ message, type, onClose, duration = 2000 }: AlertProps) => {
    const [fadeOut, setFadeOut] = useState(false);

    useEffect(() => {
        const fadeTimer = setTimeout(() => setFadeOut(true), duration - 300); // start fade out
        const removeTimer = setTimeout(onClose, duration);
        return () => {
            clearTimeout(fadeTimer);
            clearTimeout(removeTimer);
        };
    }, [onClose, duration]);

    return (
        <div className={`alert alert-${type} ${fadeOut ? 'fade-out' : ''}`}>
            <span>{message}</span>
            <button className="alert-close" onClick={onClose}>×</button>
        </div>
    );
};

export default Alert;
