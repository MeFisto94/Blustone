package com.github.mefisto94.Blustone;

public class BlockStateFlags {
    public static final int FLAG_BLOCK_UPDATE = 1;
    public static final int FLAG_SEND_CHANGES_TO_CLIENTS = 2; // (you almost always want this)
    public static final int FLAG_DONT_RERENDER = 4;//prevents the block from being re-rendered, if this is a client world.
}
