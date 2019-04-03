package com.elena.hashcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    public static List<String> slide = new ArrayList<>();

    public static void main(String[] args) {
        String path = " ... ";
        FileReader fileReader = new FileReader(path);
        try {
            HashMap<String, List<String>> horizontals = fileReader.readHorizontal();
            HashMap<String, List<String>> verticals = fileReader.readVertical();
            HashMap<String, List<String>> photos = fileReader.readPhotos();
            HashMap<String, List<String>> verticalPhotos = fileReader.readVerticalPhotos();
            if(verticals.size() != 0) {
                mergeVerticals(verticals, verticalPhotos, photos, horizontals);
            }
            createSlideShow(horizontals, photos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String outputPath = " ... ";
        FileWriter fileWriter = new FileWriter(outputPath);
        try {
            fileWriter.print(slide);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mergeVerticals(Map<String, List<String>> verticals,
                                      Map<String, List<String>> verticalPhotos,
                                      Map<String, List<String>> allPhotos,
                                      Map<String, List<String>> horizontals) {
        int size = verticalPhotos.size();
        for (int i = 0; i < size - 1; i = i + 2) {
            Set<String> pictures = verticalPhotos.keySet();
            int minNumTags = Integer.MAX_VALUE;
            String photo = "0";
            for(String picture : pictures) {
                if(verticalPhotos.get(picture).size() < minNumTags) {
                    minNumTags = verticalPhotos.get(picture).size();
                    photo = picture;
                }
            }
            System.out.println("This vertical photo: " + i);
            slideVerticals(photo, verticals, verticalPhotos, allPhotos, horizontals);
        }
    }

    public static void slideVerticals(String photo,
                                      Map<String, List<String>> verticals,
                                      Map<String, List<String>> verticalPhotos,
                                      Map<String, List<String>> allPhotos,
                                      Map<String, List<String>> horizontals) {
        List<String> tags = verticalPhotos.get(photo);
        int min;
        String minCostIndex;
        int maxCost = -1;
        String maxCostIndex = 0+"";
        for(String tag : tags) {
            List<String> neighbors = verticals.get(tag);
            if(neighbors.size() != 1) {
                SlideVertical slideVertical = new SlideVertical(photo, neighbors, tags, verticalPhotos);
                slideVertical.verticalMerge();
                min = slideVertical.getMin();
                minCostIndex = slideVertical.getMinIndex();
                if (min > maxCost) {
                    maxCost = min;
                    maxCostIndex = minCostIndex;
                }
            }
        }
        if(maxCost == -1 || maxCost == Integer.MAX_VALUE) {
            Set<String> pictures = verticalPhotos.keySet();
            int minNumTags = Integer.MAX_VALUE;
            for(String picture : pictures) {
                if(!picture.equals(photo)) {
                    if (verticalPhotos.get(picture).size() < minNumTags) {
                        minNumTags = verticalPhotos.get(picture).size();
                        maxCostIndex = picture;
                    }
                }
            }
        }
        tags.removeAll(verticalPhotos.get(maxCostIndex));
        tags.addAll(verticalPhotos.get(maxCostIndex));
        for(String tag : tags) {
            if (horizontals.containsKey(tag)) {
                horizontals.get(tag).add(photo+" "+maxCostIndex);
            }
            else {
                horizontals.put(tag, new ArrayList<>());
                horizontals.get(tag).add(photo+" "+maxCostIndex);
            }
            verticals.get(tag).remove(photo);
            verticals.get(tag).remove(maxCostIndex);
            if (verticals.get(tag).size() == 0) {
                verticals.remove(tag);
            }
        }
        allPhotos.put(photo+" "+maxCostIndex, tags);
        allPhotos.remove(photo);
        allPhotos.remove(maxCostIndex);
        verticalPhotos.remove(photo);
        verticalPhotos.remove(maxCostIndex);
    }

    public static void createSlideShow(Map<String, List<String>> horizontals,
                                       Map<String, List<String>> photos) {
        Set<String> pictures = photos.keySet();
        int minNumTags = Integer.MAX_VALUE;
        String photo = "0";
        for(String picture : pictures) {
            if(photos.get(picture).size() < minNumTags) {
                minNumTags = photos.get(picture).size();
                photo = picture;
            }
        }
        slide.add(photo);
        int size = photos.size();
        for (int i = 0; i < size - 1; i++) {
            System.out.println("This horizontal photo: " + i);
            merge(slide.get(slide.size()-1), photos, horizontals);
        }
    }

    public static void merge(String photo,
                             Map<String, List<String>> photos,
                             Map<String, List<String>> horizontals) {
        List<String> tags = photos.get(photo);
        int min;
        String minCostIndex;
        int maxCost = -1;
        String maxCostIndex = 0+"";
        for (String tag : tags) {
            List<String> neighbors = horizontals.get(tag);
            if(neighbors.size() != 1) {
                Slide slide = new Slide(photo, tags, neighbors, photos);
                slide.mergeAll();
                min = slide.getMin();
                minCostIndex = slide.getMinIndex();
                if (min > maxCost) {
                    maxCost = min;
                    maxCostIndex = minCostIndex;
                }
            }
        }
        if(maxCost == -1 || maxCost == Integer.MIN_VALUE) {
            Set<String> pictures = photos.keySet();
            int minNumTags = Integer.MAX_VALUE;
            for(String picture : pictures) {
                if(!picture.equals(photo)) {
                    if (photos.get(picture).size() < minNumTags) {
                        minNumTags = photos.get(picture).size();
                        maxCostIndex = picture;
                    }
                }
            }
        }
        slide.add(maxCostIndex);
        for(String tag : tags) {
            horizontals.get(tag).remove(photo);
            if (horizontals.get(tag).size() == 0) {
                horizontals.remove(tag);
            }
        }
        photos.remove(photo);
    }
}

class SlideVertical {
    public String photo;
    public List<String> neighbors;
    public List<String> tags;
    public Map<String, List<String>> verticalPhotos;
    public int maxCost = -1;
    public String maxCostIndex;

    public SlideVertical(String photo, List<String> neighbors, List<String> tags, Map<String, List<String>> verticalPhotos){
        this.photo = photo;
        this.neighbors = neighbors;
        this.tags = tags;
        this.verticalPhotos = verticalPhotos;
    }

    public void verticalMerge(){
        for (String neighbor : neighbors) {
            if (!neighbor.equals(photo)) {
                List<String> neighborTags = verticalPhotos.get(neighbor);
                List<String> tempP = new ArrayList<>();
                tempP.addAll(tags);
                tempP.removeAll(neighborTags);
                tempP.addAll(neighborTags);
                int cost = tempP.size();
                if (cost > maxCost) {
                    maxCost = cost;
                    maxCostIndex = neighbor;
                }
            }
        }
    }

    public int getMin(){
        return maxCost;
    }

    public String getMinIndex(){
        return maxCostIndex;
    }
}

class Slide {
    public String photo;
    public List<String> neighbors;
    public List<String> tags;
    public Map<String, List<String>> photos;
    public int maxCost = -1;
    public String maxCostIndex;

    public Slide(String photo, List<String> tags, List<String> neighbors, Map<String, List<String>> photos){
        this.photo = photo;
        this.neighbors = neighbors;
        this.tags = tags;
        this.photos = photos;
    }

    public void mergeAll(){
        for (String neighbor : neighbors) {
            if (!neighbor.equals(photo)) {
                List<String> tagsN = photos.get(neighbor);
                List<String> tempP = new ArrayList<>();
                tempP.addAll(tags);
                List<String> tempN = new ArrayList<>();
                tempN.addAll(tagsN);
                List<String> common = new ArrayList<>();
                common.addAll(tags);
                tempP.removeAll(tagsN);
                int numTagsOnlyInPhoto1 = tempP.size();
                tempN.removeAll(tags);
                int numTagsOnlyInPhotoN = tempN.size();
                common.removeAll(tempP);
                int numTagsInCommon = common.size();
                int cost = Math.min(numTagsInCommon, Math.min(numTagsOnlyInPhoto1, numTagsOnlyInPhotoN));
                if (cost > maxCost) {
                    maxCost = cost;
                    maxCostIndex = neighbor;
                }
            }
        }
    }

    public int getMin(){
        return maxCost;
    }

    public String getMinIndex(){
        return maxCostIndex;
    }
}