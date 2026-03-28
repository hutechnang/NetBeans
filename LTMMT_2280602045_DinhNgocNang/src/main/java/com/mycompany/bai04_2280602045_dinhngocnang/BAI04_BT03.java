package com.mycompany.bai04_2280602045_dinhngocnang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class BAI04_BT03 {
    private static final String URLS_FILE = "urls.txt";
    private static final String OUTPUT_FILE = "url_info.txt";
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    
    public static void main(String[] args) {
        System.out.println("=== CHUONG TRINH KIEM TRA DANH SACH URL VA LAY DIA CHI IP ===");
        System.out.println("Dang doc danh sach URL tu: " + URLS_FILE);
        
        try {
            processUrls();
            System.out.println("\nDa hoan thanh! Ket qua da duoc luu vao: " + OUTPUT_FILE);
        } catch (IOException e) {
            System.err.println("Loi khi xu ly: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void processUrls() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(URLS_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            
            String urlString;
            int successCount = 0;
            int errorCount = 0;
            
            System.out.println("\nDang xu ly cac URL...\n");
            
            writer.write("=== KET QUA KIEM TRA URL ===\n");
            writer.write(String.format("%-50s | %-20s | %-15s | %-15s\n", 
                "URL", "Dia chi IP", "So ky tu", "Thoi gian xu ly"));
            writer.write("--------------------------------------------------------------------------------------------------------\n");
            
            while ((urlString = reader.readLine()) != null) {
                urlString = urlString.trim();
                
                if (urlString.isEmpty()) {
                    continue;
                }
                
                if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                    urlString = "http://" + urlString;
                }
                
                System.out.print("Dang xu ly: " + urlString + " ... ");
                
                long startTime = System.currentTimeMillis();
                
                try {
                    String htmlContent = fetchHtmlContent(urlString);
                    int charCount = htmlContent.length();
                    
                    String ipAddress = getIpAddress(urlString);
                    
                    long processingTime = System.currentTimeMillis() - startTime;
                    String timeStr = processingTime + " ms";
                    
                    writer.write(String.format("%-50s | %-20s | %-15d | %-15s\n", 
                        urlString, ipAddress, charCount, timeStr));
                    
                    System.out.println("IP: " + ipAddress + ", So ky tu: " + charCount + ", Thoi gian: " + timeStr);
                    
                    successCount++;
                    
                } catch (UnknownHostException e) {
                    long processingTime = System.currentTimeMillis() - startTime;
                    String timeStr = processingTime + " ms";
                    
                    writer.write(String.format("%-50s | %-20s | %-15s | %-15s\n", 
                        urlString, "KHONG TIM THAY", "LOI", timeStr));
                    writer.write("  Chi tiet loi: " + e.getMessage() + "\n");
                    
                    System.out.println("LOI: Khong the tim thay dia chi IP");
                    errorCount++;
                    
                } catch (IOException e) {
                    long processingTime = System.currentTimeMillis() - startTime;
                    String timeStr = processingTime + " ms";
                    
                    writer.write(String.format("%-50s | %-20s | %-15s | %-15s\n", 
                        urlString, "LOI KET NOI", "LOI", timeStr));
                    writer.write("  Chi tiet loi: " + e.getMessage() + "\n");
                    
                    System.out.println("LOI: " + e.getMessage());
                    errorCount++;
                    
                } catch (Exception e) {
                    long processingTime = System.currentTimeMillis() - startTime;
                    String timeStr = processingTime + " ms";
                    
                    writer.write(String.format("%-50s | %-20s | %-15s | %-15s\n", 
                        urlString, "LOI KHONG XD", "LOI", timeStr));
                    writer.write("  Chi tiet loi: " + e.getMessage() + "\n");
                    
                    System.out.println("LOI: " + e.getMessage());
                    errorCount++;
                }
            }
            
            writer.flush();
            
            writer.write("\n=== TONG KET ===\n");
            writer.write("Thanh cong: " + successCount + "\n");
            writer.write("Loi: " + errorCount + "\n");
            writer.write("Tong cong: " + (successCount + errorCount) + "\n");
            
            System.out.println("\n=== TONG KET ===");
            System.out.println("Thanh cong: " + successCount);
            System.out.println("Loi: " + errorCount);
            System.out.println("Tong cong: " + (successCount + errorCount));
            
        } catch (java.io.FileNotFoundException e) {
            throw new IOException("Khong tim thay tep: " + URLS_FILE, e);
        }
    }
    
    private static String fetchHtmlContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        
        connection.setRequestProperty("User-Agent", 
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        
        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                htmlContent.append(inputLine).append("\n");
            }
        }
        
        return htmlContent.toString();
    }
    
    private static String getIpAddress(String urlString) throws UnknownHostException, IOException {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            
            InetAddress address = InetAddress.getByName(host);
            return address.getHostAddress();
            
        } catch (UnknownHostException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Khong the lay dia chi IP: " + e.getMessage(), e);
        }
    }
}

