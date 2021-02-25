package bg.sofia.uni.fmi.mjt.tagger;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import static java.lang.Character.isLetter;

public class Tagger {

    private List<City> cityAndCountry = new ArrayList<>();

    public Tagger(Reader citiesReader) {
        try (var bufferedReader = new BufferedReader(citiesReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splittedLine = line.split(",");
                cityAndCountry.add(new City(splittedLine[0], splittedLine[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void tagCities(Reader text, Writer output) {
        try (var bufferedReader = new BufferedReader(text); var bufferedWriter = new BufferedWriter(output)) {
            String line;
            boolean firstLine = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (!firstLine) {
                    bufferedWriter.write(System.lineSeparator());
                }
                firstLine = false;

                String[] splittedLine = line.split(" ");
                for (int i = 0; i < splittedLine.length - 1; i++) {
                    bufferedWriter.write(formatWord(splittedLine[i]) + " ");
                }

                bufferedWriter.write(formatWord(splittedLine[splittedLine.length - 1]));
                Character lastCharOfLine = line.charAt(line.length() - 1);

                if (lastCharOfLine.equals(' ')) {
                    bufferedWriter.write(" ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    public Collection<String> getNMostTaggedCities(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        List<String> mostTaggedCities = new ArrayList<>();
        cityAndCountry.sort(new CountOfTagsComparator());

        for (City c : cityAndCountry) {
            mostTaggedCities.add(c.getName());
            if (mostTaggedCities.size() == n) {
                return mostTaggedCities;
            }
        }
        return mostTaggedCities;
    }

    public Collection<String> getAllTaggedCities() {
        List<String> allTaggedCities = new ArrayList<>();
        for (City c : cityAndCountry) {
            if (c.getCountOfTags() > 0) {
                allTaggedCities.add(c.getName());
            }
        }
        return allTaggedCities;
    }

    public long getAllTagsCount() {
        long allTagsCount = 0L;
        for (City c : cityAndCountry) {
            if (c.getCountOfTags() > 0) {
                allTagsCount += c.getCountOfTags();
            }
        }
        return allTagsCount;
    }


    private String formatWord(String word) {
        for (City currentCity : cityAndCountry) {
            if (word.toLowerCase().contains(currentCity.getName().toLowerCase())) {
                int firstIndexOfCity = word.toLowerCase().indexOf(currentCity.getName().toLowerCase());
                int lastIndexOfCity = firstIndexOfCity + currentCity.getName().length();

                if (firstIndexOfCity > 0) {
                    char characterBeforeCity = word.charAt(firstIndexOfCity - 1);
                    if (isLetter(characterBeforeCity)) {
                        return word;
                    }
                }
                if (lastIndexOfCity < word.length() - 1) {
                    char characterAfterCity = word.charAt(lastIndexOfCity);
                    if (isLetter(characterAfterCity)) {
                        return word;
                    }
                }

                currentCity.addTag();
                return word.substring(0, firstIndexOfCity) + "<city country=\"" + currentCity.getCountry() +
                        "\">" + word.substring(firstIndexOfCity, lastIndexOfCity) +
                        "</city>" + word.substring(lastIndexOfCity);
            }
        }
        return word;
    }
}