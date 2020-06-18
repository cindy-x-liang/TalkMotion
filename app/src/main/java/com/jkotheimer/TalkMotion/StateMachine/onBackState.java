package com.jkotheimer.TalkMotion.StateMachine;

public class onBackState implements State {

    onBackState(final StateMachine sm) { this.sm = sm; }

    private final StateMachine sm;

    @Override
    public void x_move() {
        sm.say("onback_x");
    }

    @Override
    public void y_move() {
        sm.say("onback_y");
    }

    @Override
    public void z_move() {
        sm.say("onback_z");
    }
}
