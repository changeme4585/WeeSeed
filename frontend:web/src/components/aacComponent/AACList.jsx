//AAC 카드 리스트 div 컴포넌트
import React from "react";
import styled from "styled-components";
import ImgCard from "./ImgCard"; // ImgCard 카드 틀
import PropTypes from "prop-types";

// 세로축만 hidden 필수..!!
const ImgCardContainer = styled.div`
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 40px;
    width: 100%;
    max-width: 1530px;
    margin: 0px;
    overflow: auto;
    overflow-y: hidden;
`;

const CardWrapper = styled.div`
    width: 220px;
    height: 264px;
    box-sizing: border-box;
    padding: 0px;
    margin: 0px;
    cursor: pointer;
`;

const CheckboxWrapper = styled.div`
    position: absolute;
    top: 8px;
    left: 8px;
    z-index: 10;
    input[type="checkbox"] {
        width: 22px;
        height: 22px;
        cursor: pointer;
        appearance: none;
        background-color: #d9d4cf;
        border: 2px solid #767676;
        border-radius: 4px;
        transition: background-color 0.2s ease, border-color 0.2s ease;

        &:checked {
            background-color: #ffce1b;
            border-color: #ffce1b;
        }

        &:checked::before {
            content: "✔";
            display: block;
            font-size: 18px;
            font-weight: bold;
            color: black;
            text-align: center;
            line-height: 22px;
            margin-top: -2px;
        }
    }
`;

const CardContainer = styled.div`
    position: relative;
`;

const AACList = ({ imgCards, onCardClick, selectable, onSelectCard }) => {
    return (
        <ImgCardContainer>
            {imgCards.map((card, index) => (
                <CardWrapper
                    key={card.id || `default-card-id-${index}`}
                    onClick={() => {
                        if (!selectable) {
                            console.log("AACList에서의 카드:", card);
                            onCardClick(card);
                        }
                    }}
                >
                    <CardContainer>
                        {selectable && (
                            <CheckboxWrapper>
                                <input
                                    type="checkbox"
                                    onChange={(e) => onSelectCard(card, e.target.checked)}
                                />
                            </CheckboxWrapper>
                        )}
                        <div style={{ width: "100%", height: "100%" }}>
                            <ImgCard
                                imgName={card.imgName}
                                imgColor={card.imgColor}
                                imgSrc={card.imgSrc}
                                audioSrc={card.audioSrc}
                            />
                        </div>
                    </CardContainer>
                </CardWrapper>
            ))}
        </ImgCardContainer>
    );
};

AACList.propTypes = {
    imgCards: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
            imgName: PropTypes.string.isRequired,
            imgColor: PropTypes.string.isRequired,
            imgSrc: PropTypes.string.isRequired,
            audioSrc: PropTypes.string,
        })
    ).isRequired,
    onCardClick: PropTypes.func.isRequired,
    selectable: PropTypes.bool.isRequired,
    onSelectCard: PropTypes.func.isRequired,
};

export default AACList;