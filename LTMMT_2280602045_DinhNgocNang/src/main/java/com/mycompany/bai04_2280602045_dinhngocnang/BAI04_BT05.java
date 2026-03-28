package com.mycompany.bai04_2280602045_dinhngocnang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class BAI04_BT05 {
    private static final String SEED_URLS_FILE = "seed_urls.txt";
    private static final String URL_IP_LOG_FILE = "url_ip_log.txt";
    private static final String CRAWLER_LOG_FILE = "crawler.log";
    private static final String ALL_SITES_DIR = "AllSites";
    private static final int MAX_DEPTH = 2;
    private static final int MAX_URLS = 50;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    
    private static final Pattern LINK_PATTERN = Pattern.compile(
        "<a\\s+[^>]*href\\s*=\\s*[\"']([^\"']+)[\"'][^>]*>", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static Set<String> visitedUrls = new HashSet<>();
    private static int totalUrlsProcessed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== CHUONG TRINH WEB CRAWLER CO BAN ===");
        System.out.println("Do sau toi da: " + MAX_DEPTH);
        System.out.println("Gioi han so luong URL: " + MAX_URLS);
        System.out.println("Dang doc danh sach URL goc tu: " + SEED_URLS_FILE);
        
        try {
            File sitesDir = new File(ALL_SITES_DIR);
            if (!sitesDir.exists()) {
                sitesDir.mkdirs();
                System.out.println("Da tao thu muc: " + ALL_SITES_DIR);
            }
            
            List<String> seedUrls = readSeedUrls();
            
            if (seedUrls.isEmpty()) {
                System.err.println("Khong co URL nao trong tep: " + SEED_URLS_FILE);
                return;
            }
            
            crawlUrls(seedUrls, 0);
            
            System.out.println("\n=== HOAN THANH ===");
            System.out.println("Tong so URL da xu ly: " + totalUrlsProcessed);
            System.out.println("Ket qua da duoc luu vao thu muc: " + ALL_SITES_DIR);
            System.out.println("Log da duoc luu vao: " + CRAWLER_LOG_FILE);
            System.out.println("Danh sach URL-IP da duoc luu vao: " + URL_IP_LOG_FILE);
            
        } catch (IOException e) {
            System.err.println("Loi khi xu ly: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<String> readSeedUrls() throws IOException {
        List<String> urls = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SEED_URLS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    if (!line.startsWith("http://") && !line.startsWith("https://")) {
                        line = "http://" + line;
                    }
                    urls.add(line);
                }
            }
        } catch (java.io.FileNotFoundException e) {
            throw new IOException("Khong tim thay tep: " + SEED_URLS_FILE, e);
        }
        
        return urls;
    }
    
    private static void crawlUrls(List<String> urls, int depth) throws IOException {
        if (depth > MAX_DEPTH || totalUrlsProcessed >= MAX_URLS) {
            return;
        }
        
        List<String> nextLevelUrls = new ArrayList<>();
        
        for (String urlString : urls) {
            if (totalUrlsProcessed >= MAX_URLS) {
                logMessage("Da dat gioi han so luong URL toi da: " + MAX_URLS);
                break;
            }
            
            if (visitedUrls.contains(urlString)) {
                continue;
            }
            
            try {
                processUrl(urlString, depth, nextLevelUrls);
            } catch (Exception e) {
                logError(urlString, "Loi khi xu ly URL: " + e.getMessage(), e);
            }
        }
        
        if (depth < MAX_DEPTH && !nextLevelUrls.isEmpty() && totalUrlsProcessed < MAX_URLS) {
            crawlUrls(nextLevelUrls, depth + 1);
        }
    }
    
    private static void processUrl(String urlString, int depth, List<String> nextLevelUrls) 
            throws IOException {
        long startTime = System.currentTimeMillis();
        visitedUrls.add(urlString);
        totalUrlsProcessed++;
        
        System.out.println("\n[Depth " + depth + "] Dang xu ly: " + urlString);
        logMessage("Bat dau xu ly URL (Depth " + depth + "): " + urlString);
        
        try {
            String htmlContent = fetchHtmlContent(urlString);
            long fetchTime = System.currentTimeMillis() - startTime;
            
            List<String> links = extractLinks(htmlContent, urlString);
            
            String ipAddress = getIpAddress(urlString);
            
            saveHtmlContent(urlString, htmlContent);
            
            saveLinks(urlString, links);
            
            logUrlIp(urlString, ipAddress);
            
            long totalTime = System.currentTimeMillis() - startTime;
            logMessage("Hoan thanh URL: " + urlString + 
                      " | IP: " + ipAddress + 
                      " | Links: " + links.size() + 
                      " | Thoi gian: " + totalTime + " ms");
            
            System.out.println("  IP: " + ipAddress + ", Tim thay " + links.size() + " links, Thoi gian: " + totalTime + " ms");
            
            if (depth < MAX_DEPTH) {
                for (String link : links) {
                    if (!visitedUrls.contains(link) && totalUrlsProcessed < MAX_URLS) {
                        nextLevelUrls.add(link);
                    }
                }
            }
            
        } catch (IOException e) {
            long totalTime = System.currentTimeMillis() - startTime;
            logError(urlString, "Loi khi xu ly URL (mat " + totalTime + " ms): " + e.getMessage(), e);
            throw e;
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
    
    private static List<String> extractLinks(String htmlContent, String baseUrl) {
        List<String> links = new ArrayList<>();
        Set<String> uniqueLinks = new HashSet<>();
        
        try {
            Matcher matcher = LINK_PATTERN.matcher(htmlContent);
            
            while (matcher.find()) {
                String href = matcher.group(1).trim();
                
                if (href.isEmpty() || href.startsWith("javascript:") || 
                    href.startsWith("mailto:") || href.startsWith("#")) {
                    continue;
                }
                
                String absoluteUrl = resolveUrl(baseUrl, href);
                
                if (absoluteUrl != null && !uniqueLinks.contains(absoluteUrl)) {
                    uniqueLinks.add(absoluteUrl);
                    links.add(absoluteUrl);
                }
            }
        } catch (Exception e) {
            logMessage("Loi khi trich xuat links: " + e.getMessage());
        }
        
        return links;
    }
    
    private static String resolveUrl(String baseUrl, String href) {
        try {
            URL base = new URL(baseUrl);
            URL resolved = new URL(base, href);
            String resolvedUrl = resolved.toString();
            
            String protocol = resolved.getProtocol();
            if (protocol.equals("http") || protocol.equals("https")) {
                return resolvedUrl;
            }
        } catch (Exception e) {
        }
        
        return null;
    }
    
    private static String getIpAddress(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            InetAddress address = InetAddress.getByName(host);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            return "KHONG TIM THAY";
        } catch (Exception e) {
            return "LOI: " + e.getMessage();
        }
    }
    
    private static void saveHtmlContent(String urlString, String htmlContent) throws IOException {
        String fileName = sanitizeFileName(urlString) + ".html";
        File file = new File(ALL_SITES_DIR, fileName);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("<!-- URL: " + urlString + " -->\n");
            writer.write(htmlContent);
        }
    }
    
    private static void saveLinks(String urlString, List<String> links) throws IOException {
        String fileName = sanitizeFileName(urlString) + "_links.txt";
        File file = new File(ALL_SITES_DIR, fileName);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("URL: " + urlString + "\n");
            writer.write("Tong so links: " + links.size() + "\n");
            writer.write("----------------------------------------\n");
            for (String link : links) {
                writer.write(link + "\n");
            }
        }
    }
    
    private static void logUrlIp(String urlString, String ipAddress) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(URL_IP_LOG_FILE, true))) {
            writer.write(urlString + " -> " + ipAddress);
            writer.newLine();
        }
    }
    
    private static void logMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CRAWLER_LOG_FILE, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Loi khi ghi log: " + e.getMessage());
        }
    }
    
    private static void logError(String url, String errorMessage, Exception e) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CRAWLER_LOG_FILE, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            
            writer.write("[" + timestamp + "] [ERROR] URL: " + url);
            writer.newLine();
            writer.write("  Loi: " + errorMessage);
            writer.newLine();
            writer.write("  Chi tiet: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            writer.newLine();
            writer.write("---");
            writer.newLine();
        } catch (IOException ex) {
            System.err.println("Loi khi ghi log: " + ex.getMessage());
        }
    }
    
    private static String sanitizeFileName(String urlString) {
        String fileName = urlString
            .replace("http://", "")
            .replace("https://", "")
            .replace("/", "_")
            .replace("?", "_")
            .replace("&", "_")
            .replace("=", "_")
            .replace(":", "_")
            .replace("*", "_")
            .replace("|", "_")
            .replace("<", "_")
            .replace(">", "_")
            .replace("\"", "_");
        
        if (fileName.length() > 200) {
            fileName = fileName.substring(0, 200);
        }
        
        return fileName;
    }
}

