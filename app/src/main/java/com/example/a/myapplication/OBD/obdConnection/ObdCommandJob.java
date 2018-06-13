package com.example.a.myapplication.OBD.obdConnection;

/**
 * Created by Ahmed on 4/3/2018.
 */

import com.example.a.myapplication.OBD.obdApi.ObdCommand;

/**
 * This class represents a job that ObdGatewayService will have to execute and
 * maintain until the job is finished. It is, thereby, the application
 * representation of an ObdCommand instance plus a state that will be
 * interpreted and manipulated by ObdGatewayService.
 */
public class ObdCommandJob {
    private Long _id;
    private ObdCommand _command;
    private ObdCommandJobState _state;

    public ObdCommandJob(ObdCommand command) {
        _command = command;
        _state = ObdCommandJobState.NEW;
    }
    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    public ObdCommand getCommand() {
        return _command;
    }

    public ObdCommandJobState getState() {
        return _state;
    }

    public void setState(ObdCommandJobState state) {
        _state = state;
    }

    public enum ObdCommandJobState {
        NEW,
        RUNNING,
        FINISHED,
        EXECUTION_ERROR,
        BROKEN_PIPE,
        QUEUE_ERROR,
        NOT_SUPPORTED
    }

}
