package org.astrogrid.warehouse.service;

public class TalkResult {
	private String stdout;
	private String stderr;
	private int errorCode;

	public TalkResult() {
		stdout = "";
		stderr = "";
		errorCode = 0;
	}
	public TalkResult(int errorCode, String stdout, String stderr) {
    this.errorCode = errorCode;
    this.stdout = stdout;
    this.stderr = stderr;
	}
	public void setStdout(String stdout) {
		this.stdout = stdout;
	}
	public String getStdout() {
		return stdout;
	}
	public void setStderr(String stderr) {
		this.stderr = stderr;
	}
	public String getStderr() {
		return stderr;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public int getErrorCode() {
		return errorCode;
	}
}
