package com.lkq.fetchipaddress;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class IPResolverDao {
	private String host;
	private final Set<String> ipSet = new HashSet<>();
}
