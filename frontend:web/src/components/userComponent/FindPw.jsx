/*비밀번호 찾기 창*/
import React, { useState } from "react";
import styled from "styled-components";
import backButton from "../../images/svg/close-icon.svg";
// import backButton from "../../images/svg/left-arrow.svg";
import axios from "axios";

const FindPwModalBg = styled.div`
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

const FindPwModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 440px;
  height: 220px;
  background-color: #f7f6f5;
  border-radius: 15px;
  position: relative;

  h2 {
    font-size: 25px;
    font-family: "Pretendard-Bold";
    margin-bottom: 20px;
    margin-top: 25px;
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 290px;
    height: 160px;
    margin-top: 0px;
  }
`;

const FindPwButton = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 14px;
  border: none;
  width: 285px;
  height: 30px;
  margin-left: 0px;
  margin-top: 10px;
  margin-bottom: 20px;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
  }
  font-family: "Pretendard-Bold";
`;

const FindPwCloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const FindPwInput = styled.div`
  display: flex;
  flex-direction: column;

  input {
    /* input 스타일 */
    outline: none;
    width: 280px;
    height: 30px;
    border: 1px solid #rgba(255, 253, 234, 1);
    font-size: 12px;
    font-family: "Pretendard-Regular";
    box-shadow: none;
    margin-top: 2px;
    &:hover {
      background-color: rgba(255, 239, 133, 1);
    }
    &:focus {
      background-color: rgba(255, 253, 234, 1);
    }
  }
`;

const FindPw = ({ closeModal }) => {
  const [userId, setUserId] = useState(""); //입력한 유저 id
  const [newPassword, setNewPassword] = useState(""); //새 비번

  const handlePwUpdate = async () => {
    if (!userId || !newPassword) {
      alert("아이디와 새 비밀번호를 모두 입력하세요.");
      return;
    }
    //formData로 보내야하는 경우
    const data = new URLSearchParams();
    data.append("userId", userId);
    data.append("password", newPassword);

    try {
      const response = await axios.post(
        `${process.env.REACT_APP_SERVER_IP}/user/change-password?`,
        data,
        {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
        }
      );
      if (response.status === 200) {
        alert("비밀번호가 변경되었습니다!");
        closeModal();
      } else {
        alert("비밀번호 변경 실패! 아이디를 다시 확인하세요.");
      }
    } catch (error) {
      console.log("서버 오류가 발생했습니다.");
      console.error(error);
    }
  };
  return (
    <FindPwModalBg>
      <FindPwModalBox>
        <FindPwCloseButton onClick={closeModal}>
          <img src={backButton} alt="Close" />
        </FindPwCloseButton>
        <h2>비밀번호 재설정</h2>
        <FindPwInput>
          <input
            type="text"
            placeholder="아이디를 입력하세요"
            value={userId}
            onChange={(e) => setUserId(e.target.value)} //입력한 id 값
          />
          <input
            type="password"
            placeholder="새 비밀번호를 입력하세요"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          <FindPwButton onClick={handlePwUpdate}>확인</FindPwButton>
        </FindPwInput>
      </FindPwModalBox>
    </FindPwModalBg>
  );
};

export default FindPw;
