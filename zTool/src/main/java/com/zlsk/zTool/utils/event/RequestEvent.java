package com.zlsk.zTool.utils.event;

public class RequestEvent {
    /**
     * 事件标识
     */
    private int eventId;
    /**
     * 事件包含的数据
     */
    private Object data;

    /**
     * 避免因事件ID相同,而造成冲突
     */
    private Class<?> target;

    private Object passingParameters;

    public Object getPassingParameters() {
        return passingParameters;
    }

    public void setPassingParameters(Object passingParameters) {
        this.passingParameters = passingParameters;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Class<?> getTarget() {
        return target;
    }

    public void setTarget(Class<?> target) {
        this.target = target;
    }

    public RequestEvent(int eventId) {
        this.eventId = eventId;
    }

    public RequestEvent(int eventId, Object data) {
        this.eventId = eventId;
        this.data = data;
    }

    public RequestEvent(int eventId, Object data, Object passingParameters) {
        this.eventId = eventId;
        this.data = data;
        this.passingParameters = passingParameters;
    }

    public RequestEvent(Class<?> target, int eventId, Object data) {
        this.eventId = eventId;
        this.data = data;
        this.target = target;
    }

    public RequestEvent(Object data, Class<?> target) {
        this.data = data;
        this.target = target;
    }

    public RequestEvent(int eventId, Class<?> target) {
        this.eventId = eventId;
        this.target = target;
    }

    public RequestEvent() {
    }
}
