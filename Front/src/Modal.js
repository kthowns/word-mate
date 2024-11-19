// Modal.js
import React from 'react';
import './Modal.css';

function Modal({ title, onClose, children }) {
  return (
    <div className="modal-overlay">
      <div className="modal">
        <button className="close-button" onClick={onClose}>X</button>
        <h3>{title}</h3>
        <div className="modal-content">{children}</div>
      </div>
    </div>
  );
}

export default Modal;
