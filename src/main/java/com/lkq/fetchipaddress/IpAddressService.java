package com.lkq.fetchipaddress;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IpAddressService {
	public IPFetcher getFetcher(String url) {
		return new IPFetcher(url);
	}

	@RequiredArgsConstructor
	public static final class IPFetcher {
		private final String host;
		private final Set<String> ipSet = new HashSet<>();
		
		void getIpsByInet() {
			boolean isNewIp = false;
			try {
				log.info("Host->" + host);
				InetAddress[] iaddress = InetAddress.getAllByName(host);
				for (InetAddress ipaddresses : iaddress) {
					if (ipaddresses instanceof Inet4Address) {
						isNewIp = isNewIp | printIp(ipaddresses.getHostAddress());
					}
				}
			} catch (UnknownHostException e) {
				ApplicationUtil.printStack(log, e);
			}
			if(isNewIp)
				writeToFile();
			log.info("=================================");
		}

		private boolean printIp(String ip) {
			log.info(ip);
			return ipSet.add(ip);
		}

		private void writeToFile() {
			try (FileOutputStream fileOutputStream = new FileOutputStream(host + ".txt")) {
				ipSet.stream().forEach(ip -> writeToFile(fileOutputStream, ip));
			} catch (FileNotFoundException e) {
				ApplicationUtil.printStack(log, e);
			} catch (IOException e) {
				ApplicationUtil.printStack(log, e);
			}
		}

		private void writeToFile(FileOutputStream fileOutputStream, String ip) {
			try {
				fileOutputStream.write(ip.getBytes());
				fileOutputStream.write(System.getProperty("line.separator").getBytes());
			} catch (IOException e) {
				ApplicationUtil.printStack(log, e);
			}
		}

		void getIpsByNSLookup() {
			boolean isNewIp = false;
			try {
				log.info("Host->" + host);
				Resolver resolver = new SimpleResolver();
				Lookup lookup = new Lookup(host, Type.ANY);
				lookup.setResolver(resolver);

				Record[] records = lookup.run();

				if (lookup.getResult() == Lookup.SUCCESSFUL) {
					for (Record record : records) {
						if (record instanceof ARecord) {
							ARecord aRecord = (ARecord) record;
							isNewIp = isNewIp | printIp(aRecord.getAddress().getHostAddress());
						} else if (record instanceof AAAARecord) {
							AAAARecord aaaaRecord = (AAAARecord) record;
							isNewIp = isNewIp | printIp(aaaaRecord.getAddress().getHostAddress());
						} else if (record instanceof CNAMERecord) {
							CNAMERecord cnameRecord = (CNAMERecord) record;
							String canonicalName = cnameRecord.getTarget().toString();
							isNewIp = isNewIp | resolveCNAME(isNewIp, resolver, canonicalName);
						}
					}
				} else {
					log.error("DNS lookup failed: " + lookup.getErrorString());
				}
			} catch (TextParseException | UnknownHostException e) {
				ApplicationUtil.printStack(log, e);
			}
			if(isNewIp)
				writeToFile();
			log.info("=================================");
		}

		private boolean resolveCNAME(boolean isNewIp, Resolver resolver, String hostname) {
			try {
				Lookup lookup = new Lookup(hostname, Type.ANY);
				lookup.setResolver(resolver);

				Record[] records = lookup.run();

				if (lookup.getResult() == Lookup.SUCCESSFUL) {
					for (Record record : records) {
						if (record instanceof ARecord) {
							ARecord aRecord = (ARecord) record;
							isNewIp = isNewIp | printIp(aRecord.getAddress().getHostAddress());
						} else if (record instanceof AAAARecord) {
							AAAARecord aaaaRecord = (AAAARecord) record;
							isNewIp = isNewIp | printIp(aaaaRecord.getAddress().getHostAddress());
						} else if (record instanceof CNAMERecord) {
							CNAMERecord cnameRecord = (CNAMERecord) record;
							String canonicalName = cnameRecord.getTarget().toString();
							return resolveCNAME(isNewIp, resolver, canonicalName);
						}
					}
				} else {
					log.error("DNS lookup failed: " + lookup.getErrorString());
				}
			} catch (TextParseException e) {
				ApplicationUtil.printStack(log, e);
			}
			return isNewIp;
		}
	}
}
