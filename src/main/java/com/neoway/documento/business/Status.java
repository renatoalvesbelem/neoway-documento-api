package com.neoway.documento.business;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Status {
	public static AtomicLong atomicLong = new AtomicLong();

	@JsonProperty("up-time")
	private final Long upTime = ManagementFactory.getRuntimeMXBean().getUptime();
	@JsonProperty("quantidade-consultas")
	private final int consultas = atomicLong.intValue();
}
