# 필요한 라이브러리 임포트
import tensorflow as tf
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras import layers, models
import numpy as np
from tensorflow.keras.preprocessing.image import load_img, img_to_array
import glob
import os

model_save_path = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/saved_models/my_fashion_model_train_model"
model = tf.keras.models.load_model(model_save_path, compile=False)

def predict_shoe(image_path, model, class_labels, threshold=0.5):
    """이미지 경로를 받아 예측 클래스와 확률을 반환하는 함수"""
    img = load_img(image_path, target_size=(224, 224))
    img_array = img_to_array(img) / 255.0
    img_array = np.expand_dims(img_array, axis=0)

    # 다중 클래스 예측 수행
    predictions = model.predict(img_array)[0]
    predicted_index = np.argmax(predictions)
    confidence = predictions[predicted_index]

    print(predictions)
    # 예측한 클래스명과 확률 출력
    # predicted_class = class_labels[predicted_index]
    if predicted_index < len(class_labels):
        predicted_class = class_labels[predicted_index]
    else:
        print("Error: Predicted index is out of range for class labels.")
        return "신발 아님"  # 기본 반환값 설정

    print(f"{image_path}\nPredicted class: {predicted_class}, Confidence: {confidence:.2f}")

# 클래스 레이블 예시 (신발 및 기타 클래스)
# class_labels = ["Ballet Flat", "Boat", "Brogue", "Clog", "Sneaker", "Cat"]
class_labels = ["Accessories", "Apparel", "Footwear", "Free Items", "Home", "Personal Care", "Sporting Goods"]
class_labels = ['작은 옷', '옷', '신발', '증정품', '집', '씻기', '운동']

# 11. 예측 사용 예시
image_dir = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/static/images"
result1 = predict_shoe(os.path.join(image_dir, "crocs_shoe.jpg"), model, class_labels)
result2 = predict_shoe(os.path.join(image_dir, "pink_shoe.jpg"), model, class_labels)
result3 = predict_shoe(os.path.join(image_dir, "unpair_shoe.png"), model, class_labels)
result4 = predict_shoe(os.path.join(image_dir, "white_shoe.jpg"), model, class_labels)
result10 = predict_shoe(os.path.join(image_dir, "hat.jpg"), model, class_labels)
result10 = predict_shoe(os.path.join(image_dir, "pants.jpg"), model, class_labels)
result10 = predict_shoe(os.path.join(image_dir, "soap.jpeg"), model, class_labels)
result10 = predict_shoe(os.path.join(image_dir, "socks.jpeg"), model, class_labels)
result10 = predict_shoe(os.path.join(image_dir, "suit.png"), model, class_labels)

# print(result1, result2, result3, result4)