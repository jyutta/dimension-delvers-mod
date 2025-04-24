package com.wanderersoftherift.wotr.rift.objective;

public abstract class ProgressObjective extends OngoingObjective {

    public abstract int getCurrentProgress();

    public abstract int getTargetProgress();

    public boolean isComplete() {
        return getCurrentProgress() >= getTargetProgress();
    }
}
