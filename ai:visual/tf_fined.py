# 필요한 라이브러리 임포트
import tensorflow as tf
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras import layers, models
import numpy as np
from tensorflow.keras.preprocessing.image import load_img, img_to_array
import glob
import os

# 1. TFRecord 파싱 함수 정의
def parse_tfrecord(tfrecord):
    """TFRecord에서 이미지와 레이블을 파싱하는 함수"""
    feature_description = {
        'image': tf.io.FixedLenFeature([], tf.string),
        'label': tf.io.FixedLenFeature([], tf.int64),
    }
    parsed_example = tf.io.parse_single_example(tfrecord, feature_description)
    image = tf.image.decode_jpeg(parsed_example['image'], channels=3)
    image = tf.image.resize(image, [224, 224])  # MobileNetV2 입력 크기
    label = tf.cast(parsed_example['label'], tf.int32)
    return image, label

# 2. TFRecord 파일 경로 가져오기
base_dir = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/static/Shoes Dataset/TFRecords"
train_tfrecord_files = glob.glob(os.path.join(base_dir, "Train", "*.tfrecord"))
valid_tfrecord_files = glob.glob(os.path.join(base_dir, "Valid", "*.tfrecord"))
test_tfrecord_files = glob.glob(os.path.join(base_dir, "Test", "*.tfrecord"))

# 경로에 TFRecord 파일이 있는지 확인
if not train_tfrecord_files:
    print("Train TFRecord 파일이 없습니다. 경로를 확인하세요.")
else:
    print("Train TFRecord 파일 목록:", train_tfrecord_files)

# 3. 데이터셋 로드 함수 정의
def load_dataset(tfrecord_files, batch_size=32):
    """TFRecord 파일을 로드하여 배치와 프리페치 설정을 하는 함수"""
    dataset = tf.data.TFRecordDataset(tfrecord_files)
    dataset = dataset.map(parse_tfrecord, num_parallel_calls=tf.data.AUTOTUNE)
    dataset = dataset.batch(batch_size)
    dataset = dataset.prefetch(buffer_size=tf.data.AUTOTUNE)
    return dataset

# 4. 데이터셋 생성
train_dataset = load_dataset(train_tfrecord_files)
valid_dataset = load_dataset(valid_tfrecord_files)
test_dataset = load_dataset(test_tfrecord_files)

# 5. MobileNetV2 모델 구성
base_model = MobileNetV2(input_shape=(224, 224, 3), include_top=False, weights="imagenet")
base_model.trainable = False  # 사전 학습된 가중치를 고정하여 상위 레이어만 훈련

# 6. 상단 레이어 추가
model = models.Sequential([
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dense(5, activation="softmax")  # 5개의 클래스를 위한 출력 레이어
])

# 7. 모델 컴파일
model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])

# 8. 모델 훈련
history = model.fit(train_dataset, validation_data=valid_dataset, epochs=50)

# 9. 모델 평가
test_loss, test_accuracy = model.evaluate(test_dataset)
print(f"Test accuracy: {test_accuracy:.2f}")

# 12. 모델 저장
model_save_path = "/saved_models/my_shoe_model_50epoch.keras"
model.save(model_save_path)

# 13. 저장된 모델 불러오기 예시 (필요할 경우 주석 해제하여 사용)
# loaded_model = tf.keras.models.load_model(model_save_path)