import React from "react";
import addChildImage from "../../images/svg/add-child-btn.svg";

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

function AddChildButton({ onClick }) {
  return (
    <div>
      <button className="add-child-btn" onClick={onClick} style={styles.button}>
        <img
          src={addChildImage}
          style={styles.image}
          alt="아동 추가하기"
          className="addChildImage"
        />
      </button>
    </div>
  );
}

export default AddChildButton;
