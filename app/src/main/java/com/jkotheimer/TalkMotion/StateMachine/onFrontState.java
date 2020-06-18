package com.jkotheimer.TalkMotion.StateMachine;

public class onFrontState implements State {

    onFrontState(final StateMachine sm) { this.sm = sm; }

    private final StateMachine sm;

    @Override
    public void x_move() {
        sm.say("onfront_x");
    }

    @Override
    public void y_move() {
        sm.say("onfront_y");
    }

    @Override
    public void z_move() {
        sm.say("onfront_z");
    }
}
