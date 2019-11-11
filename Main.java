package webcam;

import java.io.File;

public class Main {

	public static void main(String[] args) {

		ImageDetection imgDet = new ImageDetection();

		imgDet.DetectFace();
		System.out.println(imgDet.isFaceDetected());
		if (imgDet.isFaceDetected()) {
			imgDet.takeImage();

			NewFaceTest nft = new NewFaceTest();
			try {
				String str = "C:\\Users\\Saad\\eclipse-workspace\\CollegeJava\\storedImages\\";
				File file = new File(str);
				File[] files = file.listFiles();
				for (File f : files) {
					NewFaceTest.compare(str + f.getName());
					if (nft.getResult() == true) {
						System.out.println(f.getName());
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
