package webcam;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class ImageDetection  {
	
	private String directory = "C:\\Users\\Saad\\eclipse-workspace\\CollegeJava\\takenImage\\";
	private CascadeClassifier cascadeClassifier;
	private MatOfRect detectedFaces;
	private Mat coloredImage;
	private Mat greyImage;
	private BufferedImage image;
	private int isDetected;
	private boolean isTrue = true;
	
	public ImageDetection(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.detectedFaces = new MatOfRect();
		this.coloredImage = new Mat();
		this.greyImage = new Mat();
		this.cascadeClassifier = new CascadeClassifier("C:\\Users\\Saad\\Documents\\Downloads\\facedetect\\haarcascade_frontalface_alt.xml");
	}
	
	public void DetectFace(){
		
		Mat webcamImage = new Mat();
		VideoCapture videoCapture = new VideoCapture(0);
		
		if (videoCapture.isOpened()) {
			
			while (isTrue) {
				
				videoCapture.read(webcamImage);
				
				if (!webcamImage.empty()) {

					webcamImage.copyTo(coloredImage);
					webcamImage.copyTo(greyImage);
					
					Imgproc.cvtColor(coloredImage, greyImage, Imgproc.COLOR_BGR2GRAY);
					Imgproc.equalizeHist(greyImage, greyImage);
					
					cascadeClassifier.detectMultiScale(greyImage, detectedFaces);
					
					int width = webcamImage.width(), height = webcamImage.height(), channels = webcamImage.channels();
					byte[] sourcePixels = new byte[width * height * channels];
					webcamImage.get(0, 0, sourcePixels);
				
					image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
					final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
					System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
					
					
					int accumulator = detectedFaces.toArray().length;
					
					if(accumulator > 0) {
						this.isDetected = 1;
						isTrue = false;
					}else {
						this.isDetected = 0;
					}
					
				System.out.println("Detected faces: "+this.isDetected);	
				
				
				} else {
					System.out.println("Problem");
					break;
				}
				 videoCapture.release();
			}
		}
	}
	
public boolean isFaceDetected() {
		boolean faceNumber = false;
		if(this.isDetected == 0) {
			faceNumber = false;
		}else if(this.isDetected >=1) {
			faceNumber = true;
		}
		
		return faceNumber;
	}

public void takeImage() {

		Mat webcamImage = new Mat();
		VideoCapture videoCapture = new VideoCapture(0);
		
		if(videoCapture.isOpened()) {
			
			if(videoCapture.read(webcamImage)) {
				BufferedImage image = new BufferedImage(webcamImage.width(), webcamImage.height(), BufferedImage.TYPE_3BYTE_BGR);
				
				WritableRaster raster = image.getRaster();
				DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
				byte[] data = dataBuffer.getData();
				webcamImage.get(0, 0, data);
				WritableImage writeImage = SwingFXUtils.toFXImage(image, null);
				
				String file = directory+"imageTaken.jpg";
				Imgcodecs.imwrite(file, webcamImage);
				
				videoCapture.release();
		}
	}
}

public File showTakenImage() {
	File file = new File(directory+"imageTaken.jpg");
	return file;
}

public boolean deleteTakenImage() {
	File file = new File(directory+"imageTaken.jpg");
	if(file.delete()) {
		return true;
	}else {
		return false;
	}
}

}
