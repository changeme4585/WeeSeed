import os
import numpy as np
from PIL import Image
import tensorflow as tf
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras.applications.mobilenet_v2 import preprocess_input
from tensorflow.keras.preprocessing.image import img_to_array


def preprocess_image(image, target_size=(224, 224)):
    """이미지를 전처리하여 모델 입력에 적합하게 변환합니다."""
    image = image.convert("RGB")  # 알파 채널 제거
    image = image.resize(target_size)
    image = img_to_array(image)
    image = np.expand_dims(image, axis=0)
    return preprocess_input(image)


def calculate_similarity(feature1, feature2):
    """두 이미지의 특징 벡터 간 코사인 유사도를 계산합니다."""
    return np.dot(feature1, feature2) / (np.linalg.norm(feature1) * np.linalg.norm(feature2))


def find_similar_image_local(reference_image_path, image_directory):
    """로컬 디렉토리에서 유사한 이미지를 찾습니다."""
    print("유사 이미지 검색을 시작합니다.")

    # MobileNetV2 모델 로드
    base_model = MobileNetV2(weights='imagenet', include_top=False, pooling='avg')
    print("모델 로드 완료.")

    # 참조 이미지 특징 벡터 추출
    try:
        reference_image = Image.open(reference_image_path)
        preprocessed_ref_image = preprocess_image(reference_image)
        reference_features = base_model.predict(preprocessed_ref_image)[0]
    except Exception as e:
        print(f"참조 이미지 처리 오류: {e}")
        return None

    # 디렉토리 내 모든 이미지 처리
    highest_similarity = -float('inf')
    most_similar_image = None

    for filename in os.listdir(image_directory):
        image_path = os.path.join(image_directory, filename)
        if not filename.lower().endswith(('.png', '.jpg', '.jpeg')):
            continue

        try:
            current_image = Image.open(image_path)
            preprocessed_image = preprocess_image(current_image)
            current_features = base_model.predict(preprocessed_image)[0]

            # 유사도 계산
            similarity = calculate_similarity(reference_features, current_features)
            print(f"{filename} 유사도: {similarity:.4f}")

            if similarity > highest_similarity:
                highest_similarity = similarity
                most_similar_image = filename
        except Exception as e:
            print(f"{filename} 처리 중 오류 발생: {e}")
            continue

    if most_similar_image:
        print(f"가장 유사한 이미지: {most_similar_image} (유사도: {highest_similarity:.4f})")
        return most_similar_image, highest_similarity
    else:
        print("유사한 이미지가 없습니다.")
        return None, None


# 실행 예제
if __name__ == "__main__":
    reference_image_path = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/static/images/KakaoTalk_Photo_2024-11-16-17-09-40 001.jpeg"  # 참조 이미지 경로
    image_directory = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/static/images"  # 비교할 이미지가 있는 디렉토리 경로

    similar_image, similarity = find_similar_image_local(reference_image_path, image_directory)
    if similar_image:
        print(f"가장 유사한 이미지는 '{similar_image}'입니다. 유사도: {similarity:.4f}")
    else:
        print("유사한 이미지를 찾을 수 없습니다.")
