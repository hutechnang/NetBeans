package com.mycompany.bai04_2280602045_dinhngocnang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Đinh Ngọc Năng 2280602045 22DTHG3
 */
public class BAI04_BT01 {
    private static final String DOMAINS_FILE = "domains.txt";
    private static final String OUTPUT_FILE = "domain_ip.txt";
    private static final String ERROR_LOG_FILE = "errors.log";
    private static final int CONNECTION_TIMEOUT = 5000;
    
    public static void main(String[] args) {
        System.out.println("=== CHUONG TRINH KIEM TRA DANH SACH IP TU TEP ===");
        System.out.println("Dang doc danh sach ten mien tu: " + DOMAINS_FILE);
        
        try {
            processDomains();
            System.out.println("\nDa hoan thanh! Ket qua da duoc luu vao: " + OUTPUT_FILE);
        } catch (IOException e) {
            System.err.println("Loi khi xu ly: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void processDomains() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(DOMAINS_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));
             BufferedWriter errorLog = new BufferedWriter(new FileWriter(ERROR_LOG_FILE, true))) {
            
            String domain;
            int successCount = 0;
            int errorCount = 0;
            
            System.out.println("\nDang xu ly cac ten mien...\n");
            
            while ((domain = reader.readLine()) != null) {
                domain = domain.trim();
                
                if (domain.isEmpty()) {
                    continue;
                }
                
                System.out.print("Dang kiem tra: " + domain + " ... ");
                
                try {
                    InetAddress address = getInetAddressWithTimeout(domain);
                    String ip = address.getHostAddress();
                    
                    writer.write(domain + " -> " + ip);
                    writer.newLine();
                    
                    System.out.println("IP: " + ip);
                    
                    successCount++;
                    
                } catch (UnknownHostException e) {
                    String errorMsg = "Khong the tim thay ten mien: " + domain;
                    System.out.println("LOI: " + errorMsg);
                    logError(errorLog, domain, errorMsg, e);
                    errorCount++;
                    
                } catch (IOException e) {
                    String errorMsg = "Loi ket noi hoac timeout: " + domain;
                    System.out.println("LOI: " + errorMsg);
                    logError(errorLog, domain, errorMsg, e);
                    errorCount++;
                    
                } catch (Exception e) {
                    String errorMsg = "Loi khong xac dinh: " + domain;
                    System.out.println("LOI: " + errorMsg);
                    logError(errorLog, domain, errorMsg, e);
                    errorCount++;
                }
            }
            
            writer.flush();
            errorLog.flush();
            
            System.out.println("\n=== TONG KET ===");
            System.out.println("Thanh cong: " + successCount);
            System.out.println("Loi: " + errorCount);
            System.out.println("Tong cong: " + (successCount + errorCount));
            
        } catch (java.io.FileNotFoundException e) {
            throw new IOException("Khong tim thay tep: " + DOMAINS_FILE, e);
        }
    }
    
    private static InetAddress getInetAddressWithTimeout(String domain) 
            throws UnknownHostException, IOException {
        long startTime = System.currentTimeMillis();
        
        try {
            InetAddress address = InetAddress.getByName(domain);
            
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > CONNECTION_TIMEOUT) {
                throw new IOException("Thoi gian tra cuu DNS vuot qua nguong: " + elapsedTime + "ms");
            }
            
            long reachableStartTime = System.currentTimeMillis();
            try {
                if (!address.isReachable(CONNECTION_TIMEOUT)) {
                    long reachableTime = System.currentTimeMillis() - reachableStartTime;
                    if (reachableTime > CONNECTION_TIMEOUT) {
                        return address;
                    }
                    return address;
                }
            } catch (IOException e) {
                return address;
            }
            
            return address;
            
        } catch (UnknownHostException e) {
            throw e;
        } catch (IOException e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > CONNECTION_TIMEOUT) {
                throw new IOException("Timeout khi ket noi den: " + domain + " (mat " + elapsedTime + "ms)", e);
            }
            throw e;
        }
    }
    
    private static void logError(BufferedWriter errorLog, String domain, 
                                 String errorMessage, Exception e) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        
        errorLog.write("[" + timestamp + "] ");
        errorLog.write("Domain: " + domain + " | ");
        errorLog.write("Loi: " + errorMessage);
        errorLog.newLine();
        errorLog.write("Chi tiet: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        errorLog.newLine();
        
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length > 0) {
            errorLog.write("Stack trace: " + stackTrace[0].toString());
            errorLog.newLine();
        }
        
        errorLog.write("---");
        errorLog.newLine();
    }
}

