package com.matus.lyrics.proceed;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import com.matus.lyrics.LyricsController;
import com.matus.lyrics.LyricsSelection;


public class ImageSequenceToGif {
	
	private final static String DELIMETER = "_";
	private final static String SUFIX = ".gif";
	private final static String SOURCE_FOLDER_PATH = "C:\\Users\\vha5805\\winapp\\random";
	private final static String OUTPUT_FILE_PATH = "C:\\Users\\vha5805\\winapp\\random\\" + LyricsSelection.getArtist() + DELIMETER + LyricsSelection.getSong() + SUFIX;

	
	public static void sequence()
	{
		

        List<BufferedImage> images = loadImagesFromFolder(SOURCE_FOLDER_PATH);
        
        if (images.isEmpty()) {
            System.out.println("No images found in the specified folder.");
            return;
        }

        int delayMillis = 750; // set duration of picture in second for frame
        createGifFromImages(images, OUTPUT_FILE_PATH, delayMillis);

        System.out.println("GIF creation complete.");
        removeFiles();
        System.out.println("Removed all unneccessary resources");
    }

	private static void removeFiles() {
		File folder = new File(SOURCE_FOLDER_PATH);

        // Check if the folder exists
        if (!folder.exists()) {
            System.out.println("Folder does not exist: " + SOURCE_FOLDER_PATH);
            return;
        }

        // List all files in the folder
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg") || file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                    try {
                        Files.delete(file.toPath());
                    } catch (IOException e) {
                        System.err.println("Error deleting file: " + file.getName());
                    }
                }
            }
        }
    }
		
	private static List<BufferedImage> loadImagesFromFolder(String folderPath) {
	    List<BufferedImage> images = new ArrayList<>();

	    File folder = new File(folderPath);

	    // Check if the folder exists, and if not, create it
	    if (!folder.exists()) {
	        boolean folderCreated = folder.mkdirs();
	        if (!folderCreated) {
	            System.out.println("Failed to create folder: " + folderPath);
	            return images; // Return an empty list if folder creation fails
	        }
	    }

	    File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

	    if (imageFiles != null) {
	        for (File imageFile : imageFiles) {
	            try {
	                BufferedImage image = ImageIO.read(imageFile);
	                images.add(image);
	            } catch (IOException e) {
	                System.out.println("Error loading image: " + imageFile.getName());
	            }
	        }
	    }

	    return images;
	}

    private static void createGifFromImages(List<BufferedImage> images, String outputFilePath, int delayMillis) {
        try (ImageOutputStream output = new FileImageOutputStream(new File(outputFilePath))) {
            // Set up the GIF writer
            GifSequenceWriter gifWriter = new GifSequenceWriter(output, images.get(0).getType(), delayMillis, true);

            // Write each image to the GIF
            for (BufferedImage image : images) {
                gifWriter.writeToSequence(image);
            }

            // Close the GIF writer
            gifWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
