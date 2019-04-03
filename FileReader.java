package com.elena.hashcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileReader {
    public FileReader(String path) {
        this.path = path;
    }

    public HashMap<String, List<String>> readHorizontal() throws FileNotFoundException {
        HashMap<String, List<String>> images = new HashMap<>();
        File file = new File(this.path);
        Scanner scanner = new Scanner(file);
        // Avoid first line
        int nOfPhotos = scanner.nextInt();
        int i = 0;
        while (scanner.hasNext()) {
            String str = scanner.next() + scanner.nextLine();
            if (str.charAt(0) == 'V') {
                i++;
                continue;
            }
            String[] splitted = str.split(" ");
            for (int j = 2; j < splitted.length; j++) {
                String tag = splitted[j];
                if (images.containsKey(tag)) {
                    images.get(tag).add(i+"");
                }
                else {
                    images.put(tag, new ArrayList<>());
                    images.get(tag).add(i+"");
                }
            }
            i++;
        }
        return images;
    }

    public HashMap<String, List<String>> readVertical() throws FileNotFoundException {
        HashMap<String, List<String>> images = new HashMap<>();
        File file = new File(this.path);
        Scanner scanner = new Scanner(file);
        // Avoid first line
        int nOfPhotos = scanner.nextInt();
        int i = 0;
        while (scanner.hasNext()) {
            String str = scanner.next() + scanner.nextLine();
            if (str.charAt(0) == 'H') {
                i++;
                continue;
            }
            String[] splitted = str.split(" ");
            for (int j = 2; j < splitted.length; j++) {
                String tag = splitted[j];
                if (images.containsKey(tag)) {
                    images.get(tag).add(i+"");
                }
                else {
                    images.put(tag, new ArrayList<>());
                    images.get(tag).add(i+"");
                }
            }
            i++;
        }
        return images;
    }

    public HashMap<String, List<String>> readPhotos() throws FileNotFoundException {
        HashMap<String, List<String>> images = new HashMap<>();
        File file = new File(this.path);
        Scanner scanner = new Scanner(file);
        // Avoid first line
        int nOfPhotos = scanner.nextInt();
        System.out.println(nOfPhotos + "");
        int i = 0;
        while (scanner.hasNext()) {
            String str = scanner.next() + scanner.nextLine();
            images.put(i+"", new ArrayList<>());
            String[] splitted = str.split(" ");
            for (int j = 2; j < splitted.length; j++) {
                String tag = splitted[j];
                images.get(i+"").add(tag);
            }
            i++;
        }
        return images;
    }

    public HashMap<String, List<String>> readVerticalPhotos() throws FileNotFoundException {
        HashMap<String, List<String>> images = new HashMap<>();
        File file = new File(this.path);
        Scanner scanner = new Scanner(file);
        // Avoid first line
        int nOfPhotos = scanner.nextInt();
        int i = 0;
        while (scanner.hasNext()) {
            String str = scanner.next() + scanner.nextLine();
            if (str.charAt(0) == 'H') {
                i++;
                continue;
            }
            images.put(i+"", new ArrayList<>());
            String[] splitted = str.split(" ");
            for (int j = 2; j < splitted.length; j++) {
                String tag = splitted[j];
                images.get(i+"").add(tag);
            }
            i++;
        }
        return images;
    }

    private final String path;
}