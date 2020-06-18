package com.jkotheimer.TalkMotion.StateMachine;

public class uprightState implements State {

    uprightState(final StateMachine sm) { this.sm = sm; }

    private final StateMachine sm;

    @Override
    public void x_move() {
        sm.say("upright_x");
    }

    @Override
    public void y_move() {
        sm.say("upright_y");
    }

    @Override
    public void z_move() {
        sm.say("upright_z");
    }
}
