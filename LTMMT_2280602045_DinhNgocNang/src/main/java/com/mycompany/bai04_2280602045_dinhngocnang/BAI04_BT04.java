package com.mycompany.bai04_2280602045_dinhngocnang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class BAI04_BT04 {
    private static final String WEBSITES_FILE = "websites.txt";
    private static final String ANALYSIS_FILE = "site_analysis.txt";
    private static final String IMPORTANT_SITES_LOG = "important_sites.log";
    private static final int IMPORTANT_THRESHOLD = 5;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    
    public static void main(String[] args) {
        System.out.println("=== CHUONG TRINH THU THAP VA PHAN TICH DU LIEU NHIEU TRANG WEB ===");
        System.out.println("Dang doc danh sach URL va tu khoa tu: " + WEBSITES_FILE);
        
        try {
            processWebsites();
            System.out.println("\nDa hoan thanh! Ket qua da duoc luu vao: " + ANALYSIS_FILE);
        } catch (IOException e) {
            System.err.println("Loi khi xu ly: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void processWebsites() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(WEBSITES_FILE));
             BufferedWriter analysisWriter = new BufferedWriter(new FileWriter(ANALYSIS_FILE));
             BufferedWriter importantWriter = new BufferedWriter(new FileWriter(IMPORTANT_SITES_LOG, true))) {
            
            String line;
            int successCount = 0;
            int errorCount = 0;
            int importantCount = 0;
            
            System.out.println("\nDang xu ly cac trang web...\n");
            
            analysisWriter.write("=== KET QUA PHAN TICH TRANG WEB ===\n");
            analysisWriter.write(String.format("%-50s | %-30s | %-15s | %-15s\n", 
                "URL", "Tu khoa", "So lan xuat hien", "Thoi gian tai"));
            analysisWriter.write("------------------------------------------------------------------------------------------------------------------------\n");
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\s*[|\\t,]\\s*|\\s{2,}");
                if (parts.length < 2) {
                    System.out.println("Canh bao: Dong khong hop le (thieu URL hoac tu khoa): " + line);
                    continue;
                }
                
                String urlString = parts[0].trim();
                String keyword = parts[1].trim();
                
                if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                    urlString = "http://" + urlString;
                }
                
                System.out.print("Dang xu ly: " + urlString + " (tu khoa: \"" + keyword + "\") ... ");
                
                long loadStartTime = System.currentTimeMillis();
                
                try {
                    String htmlContent = fetchHtmlContent(urlString);
                    
                    long loadTime = System.currentTimeMillis() - loadStartTime;
                    String loadTimeStr = loadTime + " ms";
                    
                    int count = countKeywordOccurrences(htmlContent, keyword);
                    
                    analysisWriter.write(String.format("%-50s | %-30s | %-15d | %-15s\n", 
                        urlString, keyword, count, loadTimeStr));
                    
                    System.out.println("Tim thay: " + count + " lan, Thoi gian tai: " + loadTimeStr);
                    
                    if (count > IMPORTANT_THRESHOLD) {
                        logImportantSite(importantWriter, urlString, keyword, count, loadTime);
                        importantCount++;
                        System.out.println("  [IMPORTANT SITE] So lan xuat hien vuot qua nguong!");
                    }
                    
                    successCount++;
                    
                } catch (IOException e) {
                    long loadTime = System.currentTimeMillis() - loadStartTime;
                    String loadTimeStr = loadTime + " ms";
                    
                    String errorMsg = "Loi khi ket noi hoac doc noi dung: " + e.getMessage();
                    analysisWriter.write(String.format("%-50s | %-30s | %-15s | %-15s\n", 
                        urlString, keyword, "LOI", loadTimeStr));
                    analysisWriter.write("  Chi tiet loi: " + errorMsg + "\n");
                    
                    System.out.println("LOI: " + errorMsg);
                    errorCount++;
                    
                } catch (Exception e) {
                    long loadTime = System.currentTimeMillis() - loadStartTime;
                    String loadTimeStr = loadTime + " ms";
                    
                    String errorMsg = "Loi khong xac dinh: " + e.getMessage();
                    analysisWriter.write(String.format("%-50s | %-30s | %-15s | %-15s\n", 
                        urlString, keyword, "LOI", loadTimeStr));
                    analysisWriter.write("  Chi tiet loi: " + errorMsg + "\n");
                    
                    System.out.println("LOI: " + errorMsg);
                    errorCount++;
                }
            }
            
            analysisWriter.flush();
            importantWriter.flush();
            
            analysisWriter.write("\n=== TONG KET ===\n");
            analysisWriter.write("Thanh cong: " + successCount + "\n");
            analysisWriter.write("Loi: " + errorCount + "\n");
            analysisWriter.write("Important sites (> " + IMPORTANT_THRESHOLD + " lan): " + importantCount + "\n");
            analysisWriter.write("Tong cong: " + (successCount + errorCount) + "\n");
            
            System.out.println("\n=== TONG KET ===");
            System.out.println("Thanh cong: " + successCount);
            System.out.println("Loi: " + errorCount);
            System.out.println("Important sites (> " + IMPORTANT_THRESHOLD + " lan): " + importantCount);
            System.out.println("Tong cong: " + (successCount + errorCount));
            
        } catch (java.io.FileNotFoundException e) {
            throw new IOException("Khong tim thay tep: " + WEBSITES_FILE, e);
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
    
    private static int countKeywordOccurrences(String content, String keyword) {
        if (content == null || keyword == null || keyword.isEmpty()) {
            return 0;
        }
        
        String lowerContent = content.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();
        
        int count = 0;
        int index = 0;
        
        while ((index = lowerContent.indexOf(lowerKeyword, index)) != -1) {
            count++;
            index += lowerKeyword.length();
        }
        
        return count;
    }
    
    private static void logImportantSite(BufferedWriter importantWriter, String url, 
                                        String keyword, int count, long loadTime) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        
        importantWriter.write("[" + timestamp + "] ");
        importantWriter.write("URL: " + url + " | ");
        importantWriter.write("Tu khoa: \"" + keyword + "\" | ");
        importantWriter.write("So lan xuat hien: " + count + " | ");
        importantWriter.write("Thoi gian tai: " + loadTime + " ms");
        importantWriter.newLine();
        importantWriter.write("---");
        importantWriter.newLine();
    }
}

