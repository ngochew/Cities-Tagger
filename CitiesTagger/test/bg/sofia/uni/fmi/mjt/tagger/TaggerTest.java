package bg.sofia.uni.fmi.mjt.tagger;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class TaggerTest {
    @Before
    public void createFileWithCityAndCountry() {
        Path filePath = Path.of("citiesAndCountries.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("Plovdiv,Bulgaria" + System.lineSeparator());
            bufferedWriter.write("Berlin,Germany" + System.lineSeparator());
            bufferedWriter.write("Bogota,Colombia" + System.lineSeparator());
            bufferedWriter.write("Paris,France" + System.lineSeparator());
            bufferedWriter.write("Sofia,Bulgaria" + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileToRead() {
        Path filePath = Path.of("toRead.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("Plovdiv's old town is a major tourist attraction. It is the second largest city " +
                    System.lineSeparator() + "in Bulgaria, after the capital ,Sofia." +
                    " Plovidva shouldn't be tagged, such as aPlovdiv. ");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileExpectedResult() {
        Path filePath = Path.of("expectedResult.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("<city country=\"Bulgaria\">Plovdiv</city>'s old " +
                    "town is a major tourist attraction. " +
                    "It is the second largest city " + System.lineSeparator() + "in Bulgaria, " +
                    "after the capital ,<city country=\"Bulgaria\">Sofia</city>." +
                    " Plovidva shouldn't be tagged, such as aPlovdiv. ");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    @Test
    public void testTaggerWorksCorrectly() {
        Path pathOfToRead = Path.of("toRead.txt");
        Path pathOfExpectedResult = Path.of("expectedResult.txt");
        Path pathOfCitysAndCountries = Path.of("citiesAndCountries.txt");
        Path pathOfResult = Path.of("result.txt");

        try (BufferedReader readerOfToRead = Files.newBufferedReader(pathOfToRead);
                BufferedReader readerOfCitysAndCountries = Files.newBufferedReader(pathOfCitysAndCountries);
                BufferedWriter writerOfResult = Files.newBufferedWriter(pathOfResult)) {
            Tagger tagger = new Tagger(readerOfCitysAndCountries);
            tagger.tagCities(readerOfToRead, writerOfResult);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        try (BufferedReader readerOfExpectedResult = Files.newBufferedReader(pathOfExpectedResult);
                BufferedReader readerOfResult = Files.newBufferedReader(pathOfResult)) {
            String expectedLine;
            String currentline;
            while ((currentline = readerOfResult.readLine()) != null) {
                expectedLine = readerOfExpectedResult.readLine();
                assertEquals(expectedLine, currentline);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileToReadCheckTagsNumbers() {
        Path filePath = Path.of("toReadCheckTags.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("Plovdiv, Sofia, Bogota, Bogota, Sofia, Bogota");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testTaggerCorrectCountOfAllTags() {
        Path pathOfToRead = Path.of("toReadCheckTags.txt");
        Path pathOfCitysAndCountries = Path.of("citiesAndCountries.txt");
        Path pathOfResult = Path.of("result.txt");

        try (BufferedReader readerOfToRead = Files.newBufferedReader(pathOfToRead);
                BufferedReader readerOfCitysAndCountries = Files.newBufferedReader(pathOfCitysAndCountries);
                BufferedWriter writerOfResult = Files.newBufferedWriter(pathOfResult)) {
            Tagger tagger = new Tagger(readerOfCitysAndCountries);
            tagger.tagCities(readerOfToRead, writerOfResult);

            assertEquals(6, tagger.getAllTagsCount());

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testReturnAllTaggedCities() {
        Path pathOfToRead = Path.of("toReadCheckTags.txt");
        Path pathOfCitysAndCountries = Path.of("citiesAndCountries.txt");
        Path pathOfResult = Path.of("result.txt");

        try (BufferedReader readerOfToRead = Files.newBufferedReader(pathOfToRead);
                BufferedReader readerOfCitysAndCountries = Files.newBufferedReader(pathOfCitysAndCountries);
                BufferedWriter writerOfResult = Files.newBufferedWriter(pathOfResult)) {
            Tagger tagger = new Tagger(readerOfCitysAndCountries);
            tagger.tagCities(readerOfToRead, writerOfResult);

            assertEquals("[Plovdiv, Bogota, Sofia]", tagger.getAllTaggedCities().toString());

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testReturnNMostTaggedCities() {
        Path pathOfToRead = Path.of("toReadCheckTags.txt");
        Path pathOfCitysAndCountries = Path.of("citiesAndCountries.txt");
        Path pathOfResult = Path.of("result.txt");

        try (BufferedReader readerOfToRead = Files.newBufferedReader(pathOfToRead);
                BufferedReader readerOfCitysAndCountries = Files.newBufferedReader(pathOfCitysAndCountries);
                BufferedWriter writerOfResult = Files.newBufferedWriter(pathOfResult)) {
            Tagger tagger = new Tagger(readerOfCitysAndCountries);
            tagger.tagCities(readerOfToRead, writerOfResult);

            assertEquals("[Bogota, Sofia]", tagger.getNMostTaggedCities(2).toString());

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileToReadSingleWord() {
        Path filePath = Path.of("toReadSingleWord.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("plovdiv");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileExpectedResultSingleWord() {
        Path filePath = Path.of("expectedResultSingleWord.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("<city country=\"Bulgaria\">plovdiv</city>");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testTaggerSingleWord() {
        Path pathOfToRead = Path.of("toReadSingleWord.txt");
        Path pathOfExpectedResult = Path.of("expectedResultSingleWord.txt");
        Path pathOfCitysAndCountries = Path.of("citiesAndCountries.txt");
        Path pathOfResult = Path.of("result.txt");

        try (BufferedReader readerOfToRead = Files.newBufferedReader(pathOfToRead);
                BufferedReader readerOfCitysAndCountries = Files.newBufferedReader(pathOfCitysAndCountries);
                BufferedWriter writerOfResult = Files.newBufferedWriter(pathOfResult)) {
            Tagger tagger = new Tagger(readerOfCitysAndCountries);
            tagger.tagCities(readerOfToRead, writerOfResult);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        try (BufferedReader readerOfExpectedResult = Files.newBufferedReader(pathOfExpectedResult);
                BufferedReader readerOfResult = Files.newBufferedReader(pathOfResult)) {
            String expectedLine;
            String currentline;
            while ((currentline = readerOfResult.readLine()) != null) {
                expectedLine = readerOfExpectedResult.readLine();
                assertEquals(expectedLine, currentline);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileWithNoCities() {
        Path filePath = Path.of("toReadNoCities.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("It's not really interesting creating test for each scenario.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileExpectedResultNoCities() {
        Path filePath = Path.of("expectedResultNoCities.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write("It's not really interesting creating test for each scenario.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testTaggerNoCities() {
        Path pathOfToRead = Path.of("toReadNoCities.txt");
        Path pathOfExpectedResult = Path.of("expectedResultNoCities.txt");
        Path pathOfCitysAndCountries = Path.of("citiesAndCountries.txt");
        Path pathOfResult = Path.of("result.txt");

        try (BufferedReader readerOfToRead = Files.newBufferedReader(pathOfToRead);
                BufferedReader readerOfCitysAndCountries = Files.newBufferedReader(pathOfCitysAndCountries);
                BufferedWriter writerOfResult = Files.newBufferedWriter(pathOfResult)) {
            Tagger tagger = new Tagger(readerOfCitysAndCountries);
            tagger.tagCities(readerOfToRead, writerOfResult);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        try (BufferedReader readerOfExpectedResult = Files.newBufferedReader(pathOfExpectedResult);
                BufferedReader readerOfResult = Files.newBufferedReader(pathOfResult)) {
            String expectedLine;
            String currentline;
            while ((currentline = readerOfResult.readLine()) != null) {
                expectedLine = readerOfExpectedResult.readLine();
                assertEquals(expectedLine, currentline);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileOneCityWithPunctuation() {
        Path filePath = Path.of("toReadOneCityWithPunctuation.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write(".plOvDiv/.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Before
    public void createFileExpectedResultOneCityWithPunctuation() {
        Path filePath = Path.of("expectedResultOneCityWithPunctuation.txt");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write(".<city country=\"Bulgaria\">plOvDiv</city>/.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testTaggerOneCityWithPunctuation() {
        Path pathOfToRead = Path.of("toReadOneCityWithPunctuation.txt");
        Path pathOfExpectedResult = Path.of("expectedResultOneCityWithPunctuation.txt");
        Path pathOfCitysAndCountries = Path.of("citiesAndCountries.txt");
        Path pathOfResult = Path.of("result.txt");

        try (BufferedReader readerOfToRead = Files.newBufferedReader(pathOfToRead);
                BufferedReader readerOfCitysAndCountries = Files.newBufferedReader(pathOfCitysAndCountries);
                BufferedWriter writerOfResult = Files.newBufferedWriter(pathOfResult)) {
            Tagger tagger = new Tagger(readerOfCitysAndCountries);
            tagger.tagCities(readerOfToRead, writerOfResult);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        try (BufferedReader readerOfExpectedResult = Files.newBufferedReader(pathOfExpectedResult);
                BufferedReader readerOfResult = Files.newBufferedReader(pathOfResult)) {
            String expectedLine;
            String currentline;
            while ((currentline = readerOfResult.readLine()) != null) {
                expectedLine = readerOfExpectedResult.readLine();
                assertEquals(expectedLine, currentline);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
