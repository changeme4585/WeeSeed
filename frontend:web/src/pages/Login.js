import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import imgLogo from "../images/Main-Logo.svg";
import "../styles/Login.css";
import coverPW from "../images/svg/closed-eyes.svg";
import openPW from "../images/svg/eyes.svg";
import ModalSignIn from "../components/userComponent/ModalSignIn";
import FindIdPw from "../components/userComponent/FindIdPw";
import LoginAlert from "../components/userComponent/LoginAlert";

function Login() {
  // State hooks
  const [credentials, setCredentials] = useState({ id: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [findModal, setFindModal] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [loginErrorMessage, setLoginErrorMessage] = useState(""); // 로그인 오류 메시지 추가
  const navigate = useNavigate();

  // 입력 값에 따른 헬퍼 함수
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({ ...prev, [name]: value })); // Update only the changed field
  };

  // 자식 정보 가져오기
  const fetchChildInfo = async (userType, userId) => {
    try {
      const childInfoUrl =
          userType === "Path"
              ? `${process.env.REACT_APP_SERVER_IP}/PathChildInfo/${userId}`
              : `${process.env.REACT_APP_SERVER_IP}/NokChildInfo/${userId}`;
      const childInfoResponse = await axios.get(childInfoUrl);
      if (childInfoResponse.status === 200) {
        localStorage.setItem(
            "child",
            JSON.stringify(childInfoResponse.data)
        );
      } else {
        console.error("아동 정보 불러오기 실패");
      }
    } catch (error) {
      console.error("아동 정보 불러오기 오류:", error);
    }
  };

  // 로그인 로직 처리
  const handleLogin = async (event) => {
    event.preventDefault();
    const { id, password } = credentials;

    // Validation
    if (!id || !password) {
      setLoginErrorMessage("아이디와 비밀번호를 모두 입력해주세요."); // 비어있으면 alert
      setShowModal(true);
      return;
    }

    try {
      const response = await axios.post(
          `${process.env.REACT_APP_SERVER_IP}/login`,
          credentials
      );

      // 사용자 유형에 따라 로직 핸들
      const userType = response.data;
      if (userType === "Path" || userType === "Nok") {
        localStorage.setItem("userType", userType);
        localStorage.setItem("userId", id);
        await fetchChildInfo(userType, id); // 자식 정보 가져오기
        navigate("/settings"); // 아동 선택 화면으로 변경
      } else {
        handleLoginFail(userType); // 사용자 유형에 따른 로그인 실패 처리
      }
    } catch (error) {
      // 오류 메시지 설정
      if (error.response && error.response.data) {
        setLoginErrorMessage(error.response.data.message || "로그인에 실패했습니다.");
      } else {
        setLoginErrorMessage("서버 오류가 발생했습니다.");
      }
      setShowModal(true); // 오류 모달 띄우기
    }
  };

  // 잘못된 아이디나 비밀번호 입력 시 로그인 실패
  const handleLoginFail = (userType) => {
    // userType에 따라 다른 메시지 설정
    if (userType === "InvalidCredentials") {
      setLoginErrorMessage("아이디나 비밀번호가 잘못되었습니다.");
    } else {
      setLoginErrorMessage("로그인에 실패했습니다. 다시 시도해 주세요.");
    }
    setShowModal(true);
  };

  // 아이디, 비밀번호 찾기 버튼 클릭
  const togglePasswordVisibility = () => setShowPassword((prev) => !prev);

  return (
      <div className="login-root">
        <div className="login-page">
          <img className="logo" src={imgLogo} alt="logo" />
          <form className="login-form" onSubmit={handleLogin}>
            <div className={`login-input ${!credentials.id ? "error" : ""}`}>
              <input
                  required
                  name="id"
                  placeholder="아이디를 입력하세요"
                  type="text"
                  value={credentials.id}
                  onChange={handleInputChange}
              />
              {!credentials.id && <div className="warning-id">* 아이디를 입력하세요.</div>}
              <div className="password-input">
                <input
                    required
                    name="password"
                    placeholder="비밀번호를 입력하세요"
                    type={showPassword ? "text" : "password"}
                    value={credentials.password}
                    onChange={handleInputChange}
                />
                <img
                    src={showPassword ? openPW : coverPW}
                    alt="Show/Hide Password"
                    className="eye-icon-login"
                    onClick={togglePasswordVisibility}
                />
              </div>
              {!credentials.password && <div className="warning-pw">* 비밀번호를 입력하세요.</div>}
            </div>
            <div className="login-bottom">
              <button className="login-btn" type="submit">로그인</button>
              <div className="bottom-btn">
                <button className="bottom" type="button" onClick={() => setModalOpen(true)}>
                  회원가입하기
                </button>
                <button className="bottom" type="button" onClick={() => setFindModal(true)}>
                  아이디/비밀번호 찾기
                </button>
              </div>
            </div>
          </form>
          {modalOpen && (
              <ModalSignIn
                  isModalOpen={modalOpen}
                  closeModal={() => setModalOpen(false)}
              />
          )}
          {showModal && <LoginAlert message={loginErrorMessage} closeModal={() => setShowModal(false)} />}
          {findModal && <FindIdPw closeModal={() => setFindModal(false)} />}
        </div>
      </div>
  );
}

export default Login;
