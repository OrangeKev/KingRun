package com.king.run.intface;

/**
 * 广播的接口
 */

public interface MyBroadcastListener {

    String ACTION_RUN_START = "king.run.start";//开始跑步广播action
    String ACTION_RUN_END = "king.run.end";//结束跑步广播action
    String ACTION_RUN_PARSE = "king.run.parse";//暂停跑步广播action
    String ACTION_RUN_RESUME = "king.run.resume";//暂停后重新开始跑步广播action

    String ACTION_RUN_CHANGED = "king.run.changed";//跑步数据发生变化action
    String EXTRA_RUNNING_STEPS = "extra.run.steps";
    String EXTRA_RUNNING_SECONDS = "extra.run.seconds";

    /**
     * 开始跑步
     */
    void startRun();

    /**
     * 结束跑步
     */
    void finishRun();

    /**
     * 暂停跑步
     */
    void pauseRun();

    /**
     * 跑步的数据发生变化
     */
    void onStepChanged(int steps, long seconds);
    /**
     * 暂停后重新开始
     */
    void onResume();
}
