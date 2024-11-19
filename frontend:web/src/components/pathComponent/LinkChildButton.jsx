import React, { useState } from "react";
import ReactDOM from "react-dom";
import linkChildImage from "../../images/svg/link-child-btn.svg";
import InputChildCode from "./InputChildCode";
import axios from "axios";

const styles = {
  button: {
    backgroundColor: "var(--white)",
    border: "none",
    cursor: "pointer",
    padding: 0,
    margin: 0,
    marginRight: "26px",
  },
  image: {
    width: "157px",
    height: "44px",
  },
};

function LinkChildButton({ fetchChildData }) {
  const [isHovered, setIsHovered] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleChildCode = () => {
    setIsModalOpen(true);
  };

  const linkChild = async (childCode) => {
    const userId = localStorage.getItem("userId");
    const formData = new FormData();
    formData.append("userId", userId);
    formData.append("childCode", childCode);

    try {
      const response = await axios.post(
        `${process.env.REACT_APP_SERVER_IP}/child/link`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      if (response.status === 200) {
        console.log("아동 연동 성공!!!");
        fetchChildData();
        setIsModalOpen(false);
      }
    } catch (error) {
      console.error("아동 연동 실패... :", error);
    }
  };

  return (
    <div>
      <button
        onClick={handleChildCode}
        style={{ ...styles.button, ...(isHovered ? styles.buttonHover : {}) }}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
      >
        <img src={linkChildImage} alt="아동 연결하기" style={styles.image} />
      </button>
      {isModalOpen &&
        ReactDOM.createPortal(
          <InputChildCode
            closeModal={() => {
              setIsModalOpen(false);
            }}
            isModalOpen={isModalOpen}
            linkChild={linkChild}
          />,
          document.body
        )}
    </div>
  );
}

export default LinkChildButton;
