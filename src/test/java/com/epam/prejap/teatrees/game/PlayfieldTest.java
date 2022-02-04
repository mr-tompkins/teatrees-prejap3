package com.epam.prejap.teatrees.game;

import com.epam.prejap.teatrees.block.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.IntStream;

@Test
public class PlayfieldTest {
    private Playfield createPlayfield(byte[][] grid) {
        Printer printer = new Printer(new PrintStream(new ByteArrayOutputStream()));
        BlockFeed blockFeed = new BlockFeed();
        Playfield playfield = new Playfield(grid.length, grid[0].length, blockFeed, printer);
        for(int i = 0; i < grid.length; ++i)
            for(int j = 0; j < grid[0].length; ++j)
                playfield.grid.fillCell(i, j, grid[i][j]);
        return playfield;
    }

    private Block mockedZBlock() {
        return new MockedBlock(new byte[][]{{1, 1, 0},
                                            {0, 1, 1}});
    }

    public void rotateBlockInEmptySpace() {
        // given
        var playfieldGrid = new byte[][]{{0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 1, 1, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 1, 1, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0}}; // {0, 0, 0, 0, 0}

        var expectedGrid  = new Grid(new byte[][]{{0, 0, 0, 0, 0},
                                                  {0, 0, 0, 0, 0},
                                                  {0, 0, 1, 0, 0},
                                                  {0, 1, 1, 0, 0},
                                                  {0, 1, 0, 0, 0}});

        Block block = mockedZBlock();

        Playfield playfield = createPlayfield(playfieldGrid);
        playfield.block = block;
        playfield.row   = 1;
        playfield.col   = 1;

        // when
        boolean movedDown = playfield.move(Move.UP);

        // then
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(movedDown);
        sa.assertEquals(playfield.row, 2);
        sa.assertEquals(playfield.col, 1);
        sa.assertTrue(playfield.block instanceof RotatedBlock);
        sa.assertEquals(playfield.grid, expectedGrid);
        sa.assertAll();
    }

    public void rotateBlockCloseToBottom() {
        // given
        var playfieldGrid = new byte[][]{{0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 1, 1, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 1, 1, 0}
                                         {0, 0, 0, 0, 0}}; // {0, 0, 0, 0, 0}

        var expectedGrid  = new Grid(new byte[][]{{0, 0, 0, 0, 0},
                                                  {0, 0, 0, 0, 0},
                                                  {0, 0, 1, 0, 0},
                                                  {0, 1, 1, 0, 0},
                                                  {0, 1, 0, 0, 0}});

        Block block = mockedZBlock();

        Playfield playfield = createPlayfield(playfieldGrid);
        playfield.block = block;
        playfield.row   = 2;
        playfield.col   = 1;

        // when
        boolean movedDown = playfield.move(Move.UP);

        // then
        SoftAssert sa = new SoftAssert();
        sa.assertFalse(movedDown);
        sa.assertEquals(playfield.row, 2);
        sa.assertEquals(playfield.col, 1);
        sa.assertTrue(playfield.block instanceof RotatedBlock);
        sa.assertEquals(playfield.grid, expectedGrid);
        sa.assertAll();
    }
    
    public void rotateBlockCloseToLeftEdge() {
        // given
        var playfieldGrid = new byte[][]{{0, 0, 0, 0, 0},  // {1, 1, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 1, 1, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0}}; // {0, 0, 0, 0, 0}
        
        var expectedGrid  = new Grid(new byte[][]{{0, 0, 0, 0, 0},
                                                  {0, 1, 0, 0, 0},
                                                  {1, 1, 0, 0, 0},
                                                  {1, 0, 0, 0, 0},
                                                  {0, 0, 0, 0, 0}});

        Block block = mockedZBlock();

        Playfield playfield = createPlayfield(playfieldGrid);
        playfield.block = block;
        playfield.row   = 0;
        playfield.col   = 0;

        // when
        boolean movedDown = playfield.move(Move.UP);

        // then
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(movedDown);
        sa.assertEquals(playfield.row, 1);
        sa.assertEquals(playfield.col, 0);
        sa.assertTrue(playfield.block instanceof RotatedBlock);
        sa.assertEquals(playfield.grid, expectedGrid);
        sa.assertAll();
    }
    
    public void rotateBlockInLimitedSpace() {
        // given
        var playfieldGrid = new byte[][]{{2, 2, 2, 2, 2},  // {2, 2, 2, 2, 2}
                                         {2, 0, 0, 2, 2},  // {2, 1, 1, 2, 2}
                                         {2, 0, 0, 0, 2},  // {2, 0, 1, 1, 2}
                                         {2, 0, 2, 2, 2},  // {2, 0, 2, 2, 2}
                                         {2, 2, 2, 2, 2}}; // {2, 2, 2, 2, 2}
        
        var expectedGrid  = new Grid(new byte[][]{{2, 2, 2, 2, 2},
                                                  {2, 0, 1, 2, 2},
                                                  {2, 1, 1, 0, 2},
                                                  {2, 1, 2, 2, 2},
                                                  {2, 2, 2, 2, 2}});

        Block block = mockedZBlock();

        Playfield playfield = createPlayfield(playfieldGrid);
        playfield.block = block;
        playfield.row   = 1;
        playfield.col   = 1;

        // when
        boolean movedDown = playfield.move(Move.UP);

        // then
        SoftAssert sa = new SoftAssert();
        sa.assertFalse(movedDown);
        sa.assertEquals(playfield.row, 1);
        sa.assertEquals(playfield.col, 1);
        sa.assertTrue(playfield.block instanceof RotatedBlock);
        sa.assertEquals(playfield.grid, expectedGrid);
        sa.assertAll();
    }

    public void rotateBlockTooCloseToBottom() {
        // given
        var playfieldGrid = new byte[][]{{0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 1, 1, 0, 0}
                                         {0, 0, 0, 0, 0}}; // {0, 0, 1, 1, 0}

        var expectedGrid  = new Grid(new byte[][]{{0, 0, 0, 0, 0},
                                                  {0, 0, 0, 0, 0},
                                                  {0, 0, 0, 0, 0},
                                                  {0, 1, 1, 0, 0},
                                                  {0, 0, 1, 1, 0}});

        Block block = mockedZBlock();

        Playfield playfield = createPlayfield(playfieldGrid);
        playfield.block = block;
        playfield.row   = 3;
        playfield.col   = 1;

        // when
        boolean movedDown = playfield.move(Move.UP);

        // then
        SoftAssert sa = new SoftAssert();
        sa.assertFalse(movedDown);
        sa.assertEquals(playfield.row, 3);
        sa.assertEquals(playfield.col, 1);
        sa.assertSame(playfield.block, block);
        sa.assertEquals(playfield.grid, expectedGrid);
        sa.assertAll();
    }
    
    public void rotateBlockTooCloseToRightEdge() {
        // given
        var playfieldGrid = new byte[][]{{0, 0, 0, 0, 0},  // {0, 0, 0, 0, 0}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 0, 1}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 1, 1}
                                         {0, 0, 0, 0, 0},  // {0, 0, 0, 1, 0}
                                         {0, 0, 0, 0, 0}}; // {0, 0, 0, 0, 0}

        var expectedGrid  = new Grid(new byte[][]{{0, 0, 0, 0, 0},
                                                  {0, 0, 0, 0, 0},
                                                  {0, 0, 0, 0, 1},
                                                  {0, 0, 0, 1, 1},
                                                  {0, 0, 0, 1, 0}});

        Block block = new RotatedBlock(mockedZBlock());

        Playfield playfield = createPlayfield(playfieldGrid);
        playfield.block = block;
        playfield.row   = 1;
        playfield.col   = 3;

        // when
        boolean movedDown = playfield.move(Move.UP);

        // then
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(movedDown);
        sa.assertEquals(playfield.row, 2);
        sa.assertEquals(playfield.col, 3);
        sa.assertSame(playfield.block, block);
        sa.assertEquals(playfield.grid, expectedGrid);
        sa.assertAll();
    }

    public void rotateBlockImpossible() {
        // given
        var playfieldGrid = new byte[][]{{2, 2, 2, 2, 2},  // {2, 2, 2, 2, 2}
                                         {2, 0, 0, 2, 2},  // {2, 1, 1, 2, 2}
                                         {2, 0, 0, 0, 2},  // {2, 0, 1, 1, 2}
                                         {2, 2, 2, 2, 2},  // {2, 0, 2, 2, 2}
                                         {2, 2, 2, 2, 2}}; // {2, 2, 2, 2, 2}

        var expectedGrid  = new Grid(new byte[][]{{2, 2, 2, 2, 2},
                                                  {2, 1, 1, 2, 2},
                                                  {2, 0, 1, 1, 2},
                                                  {2, 2, 2, 2, 2},
                                                  {2, 2, 2, 2, 2}});

        Block block = mockedZBlock();

        Playfield playfield = createPlayfield(playfieldGrid);
        playfield.block = block;
        playfield.row   = 1;
        playfield.col   = 1;

        // when
        boolean movedDown = playfield.move(Move.UP);

        // then
        SoftAssert sa = new SoftAssert();
        sa.assertFalse(movedDown);
        sa.assertEquals(playfield.row, 1);
        sa.assertEquals(playfield.col, 1);
        sa.assertSame(playfield.block, block);
        sa.assertEquals(playfield.grid, expectedGrid);
        sa.assertAll();
    }
}
