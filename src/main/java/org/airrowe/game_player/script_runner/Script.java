package org.airrowe.game_player.script_runner;

import java.time.Instant;

import org.airrowe.game_player.input_emulation.VKey;

public class Script {
    private String name;
    private Instant createTime;
    private Status status;
    private VKey startHotkey;
    private VKey stopHotkey;
    private RepeatType repeatType;
    
    
	public Script(String name, VKey startHotkey, VKey stopHotkey, RepeatType repeatType) {
		super();
		this.name = name;
		this.startHotkey = startHotkey;
		this.stopHotkey = stopHotkey;
		this.repeatType = repeatType;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Instant getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Instant createTime) {
		this.createTime = createTime;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public VKey getStartHotkey() {
		return startHotkey;
	}


	public void setStartHotkey(VKey startHotkey) {
		this.startHotkey = startHotkey;
	}


	public VKey getStopHotkey() {
		return stopHotkey;
	}


	public void setStopHotkey(VKey stopHotkey) {
		this.stopHotkey = stopHotkey;
	}


	public RepeatType getRepeatType() {
		return repeatType;
	}


	public void setRepeatType(RepeatType repeatType) {
		this.repeatType = repeatType;
	}
    
    
}
