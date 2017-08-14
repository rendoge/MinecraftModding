package harry.testmod.tabs;

import harry.testmod.init.BlockInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TestTab extends CreativeTabs
{
	public TestTab(String label) { super("testtab"); }
	public ItemStack getTabIconItem() { return new ItemStack(Item.getItemFromBlock(BlockInit.microwave_idle)); }
	
}
