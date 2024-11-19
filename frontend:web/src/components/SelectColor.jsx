import React, { useState } from "react";
import styled from "styled-components";
import upBtn from "../images/svg/up-arrow.svg";
import downBtn from "../images/svg/down-arrow.svg";

const DropdownContainer = styled.div`
  position: relative;
  display: flex;
  margin: 0px;
  width: 100%;
  max-width: ${({ small }) => (small ? "310px" : "350px")};
`;

const DropdownToggle = styled.button`
  width: ${({ small }) => (small ? "310px" : "350px")};
  height: 48px;
  padding: 5px 18px;
  border: ${({ small }) => (small ? "solid 1px #FFCE1B" : "solid 1px #FFCE1B")};
  cursor: pointer;
  background-color: #fffdea;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  font-family: "Pretendard-Regular";
  font-size: 16px;
`;

const DropdownMenu = styled.ul`
  position: absolute;
  top: 100%;
  z-index: 1000;
  display: block;
  width: 100%;
  max-width: ${({ small }) => (small ? "308px" : "348px")};
  padding: 0;
  margin-top: -2px;
  font-size: 16px;
  text-align: left;
  list-style: none;
  background-color: var(--white);
  border: ${({ small }) => (small ? "solid 1px #FFCE1B" : "solid 1px #FFCE1B")};
  max-height: ${({ $isopen }) => ($isopen ? "500px" : "0")};
  overflow: hidden;
  opacity: ${({ $isopen }) => ($isopen ? "1" : "0")};
  transition: max-height 0.3s ease, opacity 0.3s ease;
`;

const ColorIcon = styled.span`
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: ${({ color }) =>
    color === "rainbow"
      ? "linear-gradient(90deg, #F9A69E, #F4E097, #A9CDE5, #B1C8FE)"
      : color};
  margin-left: 2px;
  margin-right: 18px;
`;

const CustomArrow = styled.span`
  width: 18px;
  height: 18px;
  margin-left: auto;
  background-size: cover;
  transition: transform 0.3s ease;
  background-image: url(${({ $isopen }) => ($isopen ? upBtn : downBtn)});
  transform: ${({ $isopen }) => ($isopen ? "rotate(180deg)" : "rotate(0)")};
`;

const DropdownItem = styled.li`
  display: flex;
  align-items: center;
  font-family: "Pretendard-Regular";
  clear: both;
  font-weight: 400;
  text-align: inherit;
  white-space: nowrap;
  background: none;
  border: 0;
  padding: 0px 18px;
  cursor: pointer;
  width: ${({ small }) => (small ? "310px" : "350px")};
  height: 48px;
  color: var(--black);
  &:hover {
    background-color: #ffef85;
  }
  ${({ selected }) =>
    selected &&
    `
    background-color: var(--white);
    font-family: "Pretendard-Bold";
  `}
`;

const SelectColor = ({ setColor, small }) => {
  const [isopen, setIsOpen] = useState(false);
  const [selected, setSelected] = useState({
    label: "선택 안 함",
    color: "#767676",
  });

  const toggleDropdown = () => setIsOpen(!isopen);

  const handleSelect = (option) => {
    setSelected(option);
    if (option.color === "rainbow") {
      setColor(null);
    } else {
      setColor(option.color || null);
    }
    setIsOpen(false);
  };

  const options = [
    { label: "검정", color: "#767676" },
    { label: "빨강", color: "#F9A69E" },
    { label: "주황", color: "#F3B98B" },
    { label: "노랑", color: "#F4E097" },
    { label: "초록", color: "#92D79E" },
    { label: "파랑", color: "#A9CDE5" },
    { label: "남색", color: "#B1C8FE" },
    { label: "보라", color: "#F4AFEF" },
    !small && { label: "전체 보기", color: "rainbow" },
  ].filter(Boolean);

  return (
    <DropdownContainer small={small}>
      <DropdownToggle onClick={toggleDropdown} small={small}>
        <ColorIcon color={selected.color} /> {selected.label}{" "}
        <CustomArrow $isopen={isopen} />
      </DropdownToggle>
      <DropdownMenu $isopen={isopen} small={small}>
        {options.map((option) => (
          <DropdownItem
            key={option.label}
            onClick={() => handleSelect(option)}
            selected={selected.label === option.label}
          >
            <ColorIcon color={option.color} /> {option.label}
          </DropdownItem>
        ))}
      </DropdownMenu>
    </DropdownContainer>
  );
};

export default SelectColor;
