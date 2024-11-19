from flask import Flask, request, jsonify
import tensorflow as tf
from tensorflow.keras.models import load_model
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras.applications.mobilenet_v2 import preprocess_input
from tensorflow.keras.preprocessing.image import load_img, img_to_array
import numpy as np
from PIL import Image
import cv2
import boto3
import io
import requests

app = Flask(__name__)

# 객체 검출 모델 로드
print("객체 검출 모델을 로드 중입니다...")
object_detection_model = load_model('/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/saved_models/my_fashion_model.keras')
object_detection_model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
print("객체 검출 모델이 성공적으로 로드되었습니다.")

# 유사도 계산용 MobileNetV2 모델 로드
print("유사도 계산용 MobileNetV2 모델을 로드 중입니다...")
similarity_model = MobileNetV2(weights='imagenet', include_top=False, pooling='avg')
print("유사도 계산용 MobileNetV2 모델이 성공적으로 로드되었습니다.")

# S3 설정
S3_BASE_URL = "https://weeseed-uploads-ap-northeast-2.s3.amazonaws.com/"
s3_client = boto3.client('s3', region_name='ap-northeast-2')
s3_bucket_name = 'weeseed-uploads-ap-northeast-2'

# 클래스 레이블
class_labels = ['악세사리', '옷', '신발', '기타', '집', '씻기', '운동'] # 기타면 별도 처리


def preprocess_image(image, for_similarity=False):
    """이미지 전처리 함수 (객체 검출과 유사도 계산 모두에 사용)"""
    image = image.convert('RGB')
    resized_image = image.resize((224, 224))
    img_array = img_to_array(resized_image)
    if for_similarity:
        img_array = preprocess_input(img_array)  # MobileNetV2 전용 전처리
    else:
        img_array /= 255.0  # 객체 검출 모델 전용 정규화
    img_array = np.expand_dims(img_array, axis=0)
    return img_array


def download_image_from_s3(s3_key):
    """S3에서 이미지를 다운로드"""
    try:
        print(f"S3에서 이미지 다운로드 시도 중: {s3_key}")
        response = s3_client.get_object(Bucket=s3_bucket_name, Key=s3_key)
        image_data = response['Body'].read()
        print(f"S3에서 이미지 다운로드 성공: {s3_key}")
        return Image.open(io.BytesIO(image_data))
    except Exception as e:
        print(f"S3에서 이미지 다운로드 실패: {e}")
        return None


def is_image_valid(image):
    """이미지가 너무 밝거나 어두운지 검사"""
    grayscale_image = cv2.cvtColor(np.array(image), cv2.COLOR_RGB2GRAY)  # 이미지를 그레이스케일로 변환
    mean_gray_value = np.mean(grayscale_image)  # 그레이스케일 이미지의 평균 밝기 계산
    if mean_gray_value > 245:
        return False, "이미지가 너무 밝습니다."
    elif mean_gray_value < 15:
        return False, "이미지가 너무 어둡습니다."
    return True, ""


@app.route('/analyze-image', methods=['POST'])
def analyze_image():
    """이미지 파일을 받아 객체 분류 수행"""
    print("이미지 분석 요청이 들어왔습니다.")
    if 'file' not in request.files or request.files['file'].filename == '':
        print("유효한 파일이 제공되지 않았습니다.")
        return jsonify({'error': '유효한 파일이 제공되지 않았습니다.'}), 400

    file = request.files['file']
    try:
        # 이미지 로드 및 밝기 확인
        print("이미지를 로드 중입니다...")
        uploaded_image = Image.open(file)
        print("이미지 밝기 확인 중...")
        is_valid, error_message = is_image_valid(uploaded_image)
        if not is_valid:
            print(f"이미지 밝기 검사 실패: {error_message}")
            return jsonify({'error': error_message}), 400
        print("이미지 밝기 확인 완료.")

        # 이미지 전처리 및 예측 수행
        print("이미지 전처리 중...")
        preprocessed_image = preprocess_image(uploaded_image)
        print("이미지 예측 중...")
        predictions = object_detection_model.predict(preprocessed_image)[0]
        predicted_index = np.argmax(predictions)
        confidence = predictions[predicted_index]
        predicted_class = class_labels[predicted_index]
        print(f"예측 완료 - 예측된 클래스: {predicted_class}, 신뢰도: {confidence:.2f}")

        # 결과 반환
        return jsonify({
            'usable': True,
            'detected_object': predicted_class,
            'confidence': float(confidence)
        }), 200

    except Exception as e:
        print(f"오류 발생: {e}")
        return jsonify({'error': '내부 오류가 발생했습니다.'}), 500


@app.route('/find-similar-image', methods=['POST'])
def find_similar_image():
    """S3에서 동일한 카테고리의 유사 이미지를 찾기"""
    print("유사 이미지 검색 요청이 들어왔습니다.")
    request_data = request.get_json()
    if request_data is None:
        print("JSON 데이터가 없습니다.")
        return jsonify({"message": "요청에 JSON 데이터가 없습니다."}), 400

    representative_card_id = request_data.get('representativeCardId')
    card_category_name = request_data.get('cardName')
    original_image_path = request_data.get('imagePath')
    connected_card_image_paths = request_data.get('connectedCardPaths')

    try:
        # 원본 이미지 다운로드 및 특징 벡터 추출 (유사도 계산용 전처리)
        print(f"이미지 다운로드 중: {original_image_path}")
        response = requests.get(original_image_path)
        response.raise_for_status()
        uploaded_image = Image.open(io.BytesIO(response.content))
        preprocessed_image = preprocess_image(uploaded_image, for_similarity=True)
        uploaded_image_features = similarity_model.predict(preprocessed_image)[0]
        print("업로드된 이미지의 특징 벡터 추출 완료.")

        # S3 이미지 목록
        s3_response = s3_client.list_objects_v2(Bucket=s3_bucket_name, Prefix=f'Images/{card_category_name}')
        if 'Contents' not in s3_response:
            return jsonify({"message": "해당 카드 이미지가 없습니다."}), 404

        # 검색할 이미지 목록
        image_key = original_image_path.replace(S3_BASE_URL, "")
        connected_card_keys = [path.replace(S3_BASE_URL, "") for path in connected_card_image_paths]
        available_image_keys = [
            item['Key'] for item in s3_response['Contents']
            if item['Key'] != image_key and item['Key'] not in connected_card_keys
        ]
        print("검색할 이미지 목록:\n" + "\n".join(available_image_keys))

        # 유사도 계산 수행
        best_match_key = None
        highest_similarity = -float('inf')

        print("유사도 계산을 시작합니다.")
        for item in available_image_keys:
            s3_image = download_image_from_s3(item)
            if s3_image:
                preprocessed_s3_image = preprocess_image(s3_image, for_similarity=True)
                s3_image_features = similarity_model.predict(preprocessed_s3_image)[0]
                similarity_score = np.dot(uploaded_image_features, s3_image_features) / (
                    np.linalg.norm(uploaded_image_features) * np.linalg.norm(s3_image_features))

                print(f"이미지 비교 - {item} 유사도: {similarity_score:.4f}")
                if similarity_score > highest_similarity:
                    highest_similarity = similarity_score
                    best_match_key = item

        if best_match_key:
            print(f"가장 유사한 이미지: {best_match_key} (유사도: {highest_similarity:.4f})")
            return jsonify({"best_match": best_match_key, "similarity": float(highest_similarity)}), 200
        else:
            print("유사한 이미지가 없습니다.")
            return jsonify({"message": "유사한 이미지가 없습니다."}), 404

    except Exception as e:
        print(f"이미지 다운로드 오류: {e}")
        return jsonify({"error": "이미지를 다운로드할 수 없습니다."}), 400




if __name__ == '__main__':
    print("Flask 서버를 시작합니다...")
    app.run(port=5000, debug=True)
