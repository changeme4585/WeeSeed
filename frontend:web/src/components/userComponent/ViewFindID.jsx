/* 찾은 아이디 보여주는 팝업창 */
import React from "react";
import styled from "styled-components";
import xButton from "../../images/svg/close-icon.svg";

const ViewFindIDModalBg = styled.div`
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: transparent;
`;

const ViewFindIDModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-evenly;
  width: 480px;
  height: 242px;
  background-color: var(--click-yellow);
  border-radius: 15px;
  position: relative;
  box-shadow: 1px 3px 5px rgba(0, 0, 0, 0.2);

  h2 {
    font-size: 25px;
    height: 10px;
    font-family: "Pretendard-regular";
    margin-bottom: 0px;
  }
`;

const ViewFindIDCloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const ViewFindIDp = styled.div`
  font-size: 25px;
  font-family: "Pretendard-bold";
`;
const ViewFindIDp2 = styled.div`
  font-size: 25px;
  font-family: "Pretendard-bold";
  margin-top: -30px;
`;

const ViewFindID = ({ closeModal, userId, userName }) => {
  //서버에서 전달 받은 id 값 띄우기
  return (
    <ViewFindIDModalBg>
      <ViewFindIDModalBox>
        <ViewFindIDCloseButton onClick={closeModal}>
          <img src={xButton} alt="Close" />
        </ViewFindIDCloseButton>
        <ViewFindIDp>{`${userName}님의 아이디는`}</ViewFindIDp>
        <ViewFindIDp2>{`${userId}입니다.`}</ViewFindIDp2>
      </ViewFindIDModalBox>
    </ViewFindIDModalBg>
  );
};

export default ViewFindID;
