/*로그인 실패 창*/

import React from "react";
import styled from "styled-components";
import xButton from "../../images/svg/close-icon.svg";

const ModalBg = styled.div`
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

const ModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 440px;
  height: 150px;
  //background-color: var(--click-yellow);
    background-color: #f7f6f5;
    border-radius: 15px;
  position: relative;
  box-shadow: 1px 2px 4px var(--gray);

  h2 {
    font-size: 18px;
    height: 10px;
    font-family: "Pretendard-Bold";
  }
  p {
    padding: 10px;
    font-family: "Pretendard-Regular";
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 290px;
    margin-top: 20px;
  }
`;

const CloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const LoginAlert = ({ closeModal }) => {
  return (
    <ModalBg>
      <ModalBox>
        <CloseButton onClick={closeModal}>
          <img src={xButton} alt="Close" />
        </CloseButton>
        <h2>잘못된 아이디 또는 비밀번호입니다.</h2>
      </ModalBox>
    </ModalBg>
  );
};

export default LoginAlert;
