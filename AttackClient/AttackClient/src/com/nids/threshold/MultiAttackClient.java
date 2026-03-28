/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nids.threshold;

public class MultiAttackClient {
    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "127.0.0.1"; //IP Radmin Server
        int port = (args.length > 1) ? Integer.parseInt(args[1]) : 9090;

        int threads = (args.length > 2) ? Integer.parseInt(args[2]) : 10;
        int connectionsPerThread = (args.length > 3) ? Integer.parseInt(args[3]) : 10;
        int delayMs = (args.length > 4) ? Integer.parseInt(args[4]) : 20;

        System.out.println("Starting flood: threads=" + threads
                + " conn/thread=" + connectionsPerThread
                + " delayMs=" + delayMs);

        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < connectionsPerThread; j++) {
                    AttackClient.main(new String[]{host, String.valueOf(port)});
                    try { Thread.sleep(delayMs); } catch (InterruptedException ignored) {}
                }
            }, "atk-" + i);
            t.start();
        }
    }
}
