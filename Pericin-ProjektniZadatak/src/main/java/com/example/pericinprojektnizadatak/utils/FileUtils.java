package com.example.pericinprojektnizadatak.utils;

import com.example.pericinprojektnizadatak.HelloApplication;
import com.example.pericinprojektnizadatak.enumerations.FilePathsEnum;
import com.example.pericinprojektnizadatak.exceptions.EmptyFileException;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    static FXMLLoader registerLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
    static FXMLLoader voluntaryOrganizationLoader = new FXMLLoader(HelloApplication.class.getResource("voluntaryOrganizationForm.fxml"));




    public static String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeHashToFile(String hashedValue, String email, FilePathsEnum filePathsEnum){

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathsEnum.getFilePath(), true))) {
                writer.write(DatabaseUtils.getIDFromElement(email, filePathsEnum.getType()) + " ");
                writer.write(hashedValue);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void writeLinesToFile(List<String> lines, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> deletingAnAccountFromPasswordFile(List<String> listOfPasswords,Long id){
        return listOfPasswords.stream()
                .filter(line -> {
                    String[] splitStrings = line.split("\\s+");

                        Long currentId = Long.parseLong(splitStrings[0]);
                        if (currentId.equals(id)) {
                            // Odbacit ce ovu liniju
                            return false;
                        }

                    // zadrzat ce je
                    return true;
                })
                .collect(Collectors.toList());
    }



    public static List<String> readEveryLineFromFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(lines.isEmpty()) throw new EmptyFileException("Nothing was found in file at "+filePath);

        return lines;
    }

    public static boolean successfulLogin(String email, String password,FilePathsEnum filePathsEnum){

        List<String> passwords = readEveryLineFromFile(filePathsEnum.getFilePath());
        long idFromDatabase = DatabaseUtils.getIDFromElement(email, filePathsEnum.getType());

        for(String tempPassword:passwords){
            String[] substrings = tempPassword.split("\\s+");
            long passwordIndex = Long.parseLong(substrings[0]);
            String hashedPassword = substrings[1];


            if(passwordIndex == idFromDatabase && Objects.requireNonNull(hashSHA256(password)).compareTo(hashedPassword) == 0)
                return true;


        }

        return false;
    }

    public static void writeActiveUserToFile(String email,FilePathsEnum filePathsEnum){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FilePathsEnum.ACTIVE_USER_FILE_PATH.getFilePath(),false))) {

            writer.write(email);
            writer.newLine();
            writer.write(filePathsEnum.getType());


            System.out.println("Active user written successfully!");
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static Optional<DataChanges> readCurrentData(){
        Optional<DataChanges>  dataChangeStoreUtil = null;
        try{
            FileInputStream file = new FileInputStream("E:/Faks/3. semestar/Java/Volonterske_akcije_Pericin/Pericin-ProjektniZadatak/files/serialization.bin");
            ObjectInputStream in = new ObjectInputStream(file);

            DataChanges dataWrapper;
            dataWrapper = (DataChanges) in.readObject();
            System.out.println(dataWrapper);
            dataChangeStoreUtil = Optional.of(dataWrapper);

            System.out.println(dataChangeStoreUtil.get().getChangesList());
            in.close();
            file.close();

        }catch(IOException e)
        {
            System.out.println(e);
            logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return dataChangeStoreUtil;
    }

}
