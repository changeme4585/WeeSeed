import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/SignInP.css";
import axios from "axios";
import coverPW from "../images/svg/closed-eyes.svg";
import openPW from "../images/svg/eyes.svg";

function SignInP() {
  const [formData, setFormData] = useState({
    name: "",
    pathologistId: "",
    password: "",
    confirmPassword: "",
    email: "",
    organization: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const navigate = useNavigate();

  // 입력값 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  // 폼 제출 핸들러
  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Submitting form...");
    const validationErrors = validateFormData(formData);

    // 유효성 검사 통과 시 제출
    if (Object.keys(validationErrors).length === 0) {
      submitForm(formData);
    } else {
      console.log("Validation errors:", validationErrors);
    }
  };

  // 폼 데이터 유효성 검사
  const validateFormData = (data) => {
    const errors = {};

    // ID 체크
    if (data.pathologistId.length < 5 || data.pathologistId.length > 15) {
      errors.pathologistId = "ID는 5~15자여야 합니다.";
    }

    // 비밀번호 체크
    if (!validatePassword(data.password)) {
      errors.password = "비밀번호는 10자 이상의 영문+숫자여야 합니다.";
    }

    // 비밀번호 재입력 체크
    if (data.password !== data.confirmPassword) {
      errors.confirmPassword = "비밀번호가 일치하지 않습니다.";
    }

    // 이메일 형식 체크
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
      errors.email = "유효한 이메일을 입력해주세요.";
    }

    return errors; // 검사 결과 반환
  };

  // 비밀번호 유효성 검사
  const validatePassword = (password) => {
    return password.length >= 10 && /(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]/.test(password);
  };

  /*
   * 테스트 : "https://jsonplaceholder.typicode.com/posts"
   * 서버 : "/pathSignIn"
   * 서버에 회원가입 정보 제출 함수
   */
  const submitForm = async (data) => {
    try {
      const response = await axios.post(
          `${process.env.REACT_APP_SERVER_IP}/pathSignIn`,
          {
            pathologistId: data.pathologistId,
            password: data.password,
            email: data.email,
            organizationName: data.organization,
            name: data.name,
          }
      );

      if (response.status === 200) {
        console.log("회원가입 성공");
        navigate("/");  // 홈으로 이동
      } else {
        console.log("회원가입 실패:", response.data); // 실패 시 로그
      }
    } catch (error) {
      console.error("회원가입 오류:", error);  // 오류 처리
    }
  };

  // 비밀번호 보이기 / 숨기기 토글 핸들러
  const togglePasswordVisibility = () => setShowPassword((prev) => !prev);
  const toggleConfirmPasswordVisibility = () => setShowConfirmPassword((prev) => !prev);

  return (
      <div className="signin-path-root">
        <div className="signin-page">
          <h2 className="sign-in-h2">회원가입하기</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <InputField
                  name="pathologistId"
                  type="text"
                  value={formData.pathologistId}
                  placeholder="아이디를 입력하세요"
                  onChange={handleChange}
                  required
                  minLength={5}
                  maxLength={15}
              />
              <PasswordField
                  name="password"
                  value={formData.password}
                  placeholder="비밀번호를 입력하세요"
                  onChange={handleChange}
                  showPassword={showPassword}
                  togglePasswordVisibility={togglePasswordVisibility}
                  required
              />
              <PasswordField
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  placeholder="비밀번호를 다시 입력하세요"
                  onChange={handleChange}
                  showPassword={showConfirmPassword}
                  togglePasswordVisibility={toggleConfirmPasswordVisibility}
                  required
              />
              <InputField
                  name="name"
                  type="text"
                  value={formData.name}
                  placeholder="이름을 입력하세요"
                  onChange={handleChange}
                  required
              />
              <InputField
                  name="email"
                  type="email"
                  value={formData.email}
                  placeholder="이메일을 입력하세요"
                  onChange={handleChange}
                  required
              />
              <InputField
                  name="organization"
                  type="text"
                  value={formData.organization}
                  placeholder="소속기관을 입력하세요"
                  onChange={handleChange}
                  required
              />
            </div>
            <button className="sign-btn" type="submit">회원가입</button>
          </form>
        </div>
      </div>
  );
}

// 입력 필드 컴포넌트
const InputField = ({ name, type, value, placeholder, onChange, required, minLength, maxLength }) => (
    <input
        name={name}
        type={type}
        value={value}
        placeholder={placeholder}
        onChange={onChange}
        required={required}
        minLength={minLength}
        maxLength={maxLength}
    />
);

// 비밀번호 필드 컴포넌트
const PasswordField = ({ name, value, placeholder, onChange, showPassword, togglePasswordVisibility, required }) => (
    <div className="pw-input">
      <input
          name={name}
          type={showPassword ? "text" : "password"}
          value={value}
          placeholder={placeholder}
          onChange={onChange}
          required={required}
      />
      <img
          src={showPassword ? openPW : coverPW}
          alt="Show/Hide Password"
          className="eye-icon"
          onClick={togglePasswordVisibility}
      />
    </div>
);

export default SignInP;
