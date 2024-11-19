/*gpt 응답 대기 애니메이션*/
import React from "react";

const LoadingIndicator = ({ color = "#f7f6f5", size = 8, spacing = 8 }) => {
    const containerStyle = {
        display: "inline-flex",
        gap: `${spacing}px`,
        padding: "8px",
    };

    const dotStyle = {
        width: `${size}px`,
        height: `${size}px`,
        borderRadius: "50%",
        backgroundColor: color,
    };

    return (
        <div style={containerStyle}>
            <style>
                {`
          @keyframes dotPulse {
            0% { transform: scale(0.8); opacity: 0.5; }
            100% { transform: scale(1.2); opacity: 1; }
          }
          .loading-dot {
            animation: dotPulse 0.6s ease-in-out infinite alternate;
          }
        `}
            </style>
            <div
                className="loading-dot"
                style={{ ...dotStyle, animationDelay: "0s" }}
            ></div>
            <div
                className="loading-dot"
                style={{ ...dotStyle, animationDelay: "0.2s" }}
            ></div>
            <div
                className="loading-dot"
                style={{ ...dotStyle, animationDelay: "0.4s" }}
            ></div>
        </div>
    );
};

export default LoadingIndicator;