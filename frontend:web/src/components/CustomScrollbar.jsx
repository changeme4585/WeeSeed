import React from "react";
import { Scrollbar } from "react-scrollbars-custom";

const CustomScrollbar = ({ children }) => {
  return (
    <Scrollbar
      style={{
        width: "100%",
        height: "100vh",
        background: "#FFFFFF",
      }}
      trackYProps={{
        renderer: (props) => {
          const { elementRef, ...restProps } = props;
          return (
            <span
              {...restProps}
              ref={elementRef}
              style={{
                width: "8px",
                background: "#FFFFFF",
                borderRadius: "3px",
                position: "absolute",
                right: 0,
                top: 0,
                bottom: 0,
              }}
              className="custom-track"
            />
          );
        },
      }}
      thumbYProps={{
        renderer: (props) => {
          const { elementRef, ...restProps } = props;
          return (
            <span
              {...restProps}
              ref={elementRef}
              style={{
                width: "8px",
                background: "#999999",
                borderRadius: "10px",
                position: "absolute",
                top: 0,
              }}
              className="custom-thumb"
            />
          );
        },
      }}
    >
      <div className="custom-scrollbar-container">{children}</div>
    </Scrollbar>
  );
};

export default CustomScrollbar;
