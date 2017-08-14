package harry.testmod.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class TestBlock extends Block 
{
	public TestBlock(String name)
	{
		super(Material.IRON);
		setUnlocalizedName(name);
		setRegistryName(name);
	}

}
