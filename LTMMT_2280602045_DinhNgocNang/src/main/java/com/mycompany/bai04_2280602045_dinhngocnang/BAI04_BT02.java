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
public class BAI04_BT02 {
    private static final String URLS_FILE = "urls.txt";
    private static final String RESULTS_FILE = "results.txt";
    private static final String HOT_SITES_LOG = "hot_sites.log";
    private static final int HOT_THRESHOLD = 10;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    
    public static void main(String[] args) {
        System.out.println("=== CHUONG TRINH TIM KIEM TU KHOA TREN TRANG WEB ===");
        
        String keyword = "";
        if (args.length > 0) {
            keyword = args[0];
        } else {
            System.out.print("Nhap tu khoa can tim kiem: ");
            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                keyword = scanner.nextLine().trim();
            }
        }
        
        if (keyword.isEmpty()) {
            System.err.println("Loi: Tu khoa khong duoc de trong!");
            return;
        }
        
        System.out.println("Tu khoa: " + keyword);
        System.out.println("Dang doc danh sach URL tu: " + URLS_FILE);
        
        try {
            processUrls(keyword);
            System.out.println("\nDa hoan thanh! Ket qua da duoc luu vao: " + RESULTS_FILE);
        } catch (IOException e) {
            System.err.println("Loi khi xu ly: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void processUrls(String keyword) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(URLS_FILE));
             BufferedWriter resultsWriter = new BufferedWriter(new FileWriter(RESULTS_FILE));
             BufferedWriter hotSitesWriter = new BufferedWriter(new FileWriter(HOT_SITES_LOG, true))) {
            
            String urlString;
            int successCount = 0;
            int errorCount = 0;
            int hotSitesCount = 0;
            
            System.out.println("\nDang xu ly cac URL...\n");
            
            while ((urlString = reader.readLine()) != null) {
                urlString = urlString.trim();
                
                if (urlString.isEmpty()) {
                    continue;
                }
                
                if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                    urlString = "http://" + urlString;
                }
                
                System.out.print("Dang xu ly: " + urlString + " ... ");
                
                try {
                    String htmlContent = fetchHtmlContent(urlString);
                    
                    int count = countKeywordOccurrences(htmlContent, keyword);
                    
                    resultsWriter.write(urlString + " -> " + count + " lan");
                    resultsWriter.newLine();
                    
                    System.out.println("Tim thay: " + count + " lan");
                    
                    if (count > HOT_THRESHOLD) {
                        logHotSite(hotSitesWriter, urlString, count, keyword);
                        hotSitesCount++;
                        System.out.println("  [HOT SITE] So lan xuat hien vuot qua nguong!");
                    }
                    
                    successCount++;
                    
                } catch (IOException e) {
                    String errorMsg = "Loi khi ket noi hoac doc noi dung: " + urlString;
                    System.out.println("LOI: " + errorMsg);
                    
                    resultsWriter.write(urlString + " -> 0 lan (LOI: " + e.getMessage() + ")");
                    resultsWriter.newLine();
                    
                    errorCount++;
                    
                } catch (Exception e) {
                    String errorMsg = "Loi khong xac dinh: " + urlString;
                    System.out.println("LOI: " + errorMsg);
                    
                    resultsWriter.write(urlString + " -> 0 lan (LOI: " + e.getMessage() + ")");
                    resultsWriter.newLine();
                    
                    errorCount++;
                }
            }
            
            resultsWriter.flush();
            hotSitesWriter.flush();
            
            System.out.println("\n=== TONG KET ===");
            System.out.println("Thanh cong: " + successCount);
            System.out.println("Loi: " + errorCount);
            System.out.println("Hot sites (> " + HOT_THRESHOLD + " lan): " + hotSitesCount);
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
    
    private static void logHotSite(BufferedWriter hotSitesWriter, String url, 
                                   int count, String keyword) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        
        hotSitesWriter.write("[" + timestamp + "] ");
        hotSitesWriter.write("URL: " + url + " | ");
        hotSitesWriter.write("Tu khoa: \"" + keyword + "\" | ");
        hotSitesWriter.write("So lan xuat hien: " + count);
        hotSitesWriter.newLine();
        hotSitesWriter.write("---");
        hotSitesWriter.newLine();
    }
}

