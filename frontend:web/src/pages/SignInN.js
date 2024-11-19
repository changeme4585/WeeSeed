import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/SignIn.css";
import axios from "axios";
import coverPW from "../images/svg/closed-eyes.svg";
import openPW from "../images/svg/eyes.svg";

function SignInN() {
  // State Hook : 폼 데이터와 비밀번호 가시성 관리
  const [formData, setFormData] = useState({
    name: "",
    nokId: "",
    password: "",
    confirmPassword: "",
    email: "",
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const navigate = useNavigate(); // 페이지 이동을 위한 navigate 함수

  // 입력 값 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value, // 변경된 필드만 업데이트
    }));
  };

  // 폼 제출 핸들러
  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Submitting form...");
    const validationErrors = validateFormData(formData); // 유효성 검사

    // 유효성 검사 통과 시 제출
    if (Object.keys(validationErrors).length === 0) {
      submitForm(formData);
    } else {
      console.log("Validation errors:", validationErrors); // 에러 로그
    }
  };

  // 폼 데이터 유효성 검사 함수
  const validateFormData = (data) => {
    const errors = {};

    // 아이디 유효성 검사
    if (data.nokId.length < 5 || data.nokId.length > 15) {
      errors.nokId = "ID는 5~15자여야 합니다.";
    }
    // 비밀번호 유효성 검사
    if (
        data.password.length < 10 ||
        !/(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]/.test(data.password)
    ) {
      errors.password = "비밀번호는 10자 이상의 영문+숫자여야 합니다.";
    }

    // 비밀번호 재입력 검사
    if (data.password !== data.confirmPassword) {
      errors.confirmPassword = "비밀번호가 일치하지 않습니다.";
    }

    // 이메일 형식 검사
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
      errors.email = "유효한 이메일을 입력해주세요.";
    }

    return errors; // 검사 결과 반환
  };

  /*
   * 테스트 : "https://jsonplaceholder.typicode.com/posts"
   * 서버 : "/nokSignIn"
   * 서버에 회원가입 정보 제출 함수
   */
  const submitForm = async (data) => {
    try {
      console.log("Submitting form with data:", data);
      const response = await axios.post(
          `${process.env.REACT_APP_SERVER_IP}/nokSignIn`,
          {
            nokId: data.nokId,
            password: data.password,
            email: data.email,
            name: data.name,
          }
      );

      if (response.status === 200) {
        // 회원가입 성공 시
        console.log("회원가입 성공");
        navigate("/"); // 홈으로 이동
      } else {
        console.log("회원가입 실패:", response.data); // 실패 시 로그
      }
    } catch (error) {
      console.error("회원가입 오류:", error); // 오류 처리
    }
  };

  return (
      <div className="signin-nok-root">
        <div className="signin-nok-page">
          <h2 className="sign-in-nok-h2">회원가입하기</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-nok-group">
              <input
                  required
                  type="text"
                  name="nokId"
                  value={formData.nokId}
                  minLength={5}
                  maxLength={15}
                  onChange={handleChange}
                  placeholder="아이디를 입력하세요"
              />
              <div className="pw-nok-input">
                <input
                    required
                    placeholder="비밀번호를 입력하세요"
                    type={showPassword ? "text" : "password"}
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                />
                <img
                    src={showPassword ? openPW : coverPW}
                    alt="Show/Hide Password"
                    className="eye-icon"
                    onClick={() => setShowPassword(!showPassword)} // 비밀번호 가시성 토글
                />
              </div>
              <div className="pw-nok-confirm-input">
                <input
                    required
                    type={showConfirmPassword ? "text" : "password"}
                    name="confirmPassword"
                    minLength={10}
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    placeholder="비밀번호를 다시 입력하세요"
                />
                <img
                    src={showConfirmPassword ? openPW : coverPW}
                    alt="Show/Hide Password"
                    className="eye-nok-icon"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)} // 재입력 비밀번호 가시성 토글
                />
              </div>
              <input
                  required
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  placeholder="이름을 입력하세요"
              />
              <input
                  required
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="이메일을 입력하세요"
              />
            </div>
            <button className="sign-nok-btn" type="submit">
              회원가입
            </button>
          </form>
        </div>
      </div>
  );
}

export default SignInN;