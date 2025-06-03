package net.mj.bundletech.bundledump;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class DispenseBundle extends ItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer dispenserPointer, ItemStack bundle) {
        /// Get bundle info
        BundleContentsComponent bundleContents = bundle.get(DataComponentTypes.BUNDLE_CONTENTS);
        // If bundle is null, return before doing anything
        if (bundleContents == null) {
            return bundle;
        }
        BundleContentsComponent.Builder bundleBuilder = new BundleContentsComponent.Builder(bundleContents);

        /// Get dispenser info
        Direction direction = dispenserPointer.state().get(DispenserBlock.FACING);
        BlockPos dispenserPos = dispenserPointer.pos();
        BlockPos targetPos = dispenserPos.offset(direction);
        World world = dispenserPointer.world();
        BlockEntity targetBE = world.getBlockEntity(targetPos); // Is null if not a block entity


        /// If pointing to a furnace-like container
        if (targetBE instanceof AbstractFurnaceBlockEntity targetFurn) {
            // Decide which slot to use based on which side the dispenser is facing into (same logic as with hoppers, except input items can be pulled out)
            int slotIndex;
            if      (direction == Direction.UP)   { slotIndex = 2; }
            else if (direction == Direction.DOWN) { slotIndex = 0; }
            else                                  { slotIndex = 1; }
            ItemStack slotStack = targetFurn.getStack(slotIndex);

            // If the slot is empty, return before doing anything
            if (slotStack.isEmpty()) {
                return bundle;
            }

            // Add slot contents to bundle
            int amountAdded = bundleBuilder.add(slotStack);
            // Subtract the added contents from the slot
            if (amountAdded > 0) {
                slotStack = slotStack.split(amountAdded);
                targetFurn.setStack(slotIndex, slotStack);
            }

            // Mark that the BE has changed
            targetFurn.markDirty();
        }
        /// If pointing into any other kind of container (targetInv)
        else if (targetBE instanceof Inventory targetInv) {
            int slotIndex;

            // If the inventory is sided (e.g. furnace), search only in available slots for the first occupied slot
            if (targetBE instanceof SidedInventory sidedInv) {
                int[] slotIndices = sidedInv.getAvailableSlots(direction.getOpposite());

                int i;
                for (i = 0; i < slotIndices.length; i++) {
                    ItemStack slotStack = targetInv.getStack(i);
                    if ( !slotStack.isEmpty() ) { break; }
                }

                // Do nothing if target inventory is completely empty
                if (i >= slotIndices.length) {
                    return bundle;
                }

                slotIndex = slotIndices[i];
            }
            // If not sided, search through all slots for first occupied slot
            else {
                // Find first non-empty slot
                for (slotIndex = 0; slotIndex < targetInv.size(); slotIndex++) {
                    ItemStack slotStack = targetInv.getStack(slotIndex);
                    if ( !slotStack.isEmpty() ) { break; }
                }

                // Do nothing if target inventory is completely empty
                if (slotIndex >= targetInv.size()) {
                    return bundle;
                }
            }

            ItemStack slotStack = targetInv.getStack(slotIndex);

            // Add slot contents to bundle
            int amountAdded = bundleBuilder.add(slotStack);
            // Subtract the added contents from the slot
            if (amountAdded > 0) {
                slotStack = slotStack.split(amountAdded);
                targetInv.setStack(slotIndex, slotStack);

                // If pulled any, play insert sound
                world.playSound(
                    null,
                    dispenserPos,
                    SoundEvents.ITEM_BUNDLE_INSERT,
                    SoundCategory.BLOCKS,
                    1.0F,
                    1.0F
                );
            }
            else {
                // If no items to pull, play insert_fail sound
                world.playSound(
                    null,
                    dispenserPos,
                    SoundEvents.ITEM_BUNDLE_INSERT_FAIL,
                    SoundCategory.BLOCKS,
                    1.0F,
                    1.0F
                );
            }

            // Mark that the BE has changed
            targetBE.markDirty();
        }
        ///  If not pointing into container
        else {
            // If bundle is empty, return before doing anything
            if (bundleContents.isEmpty()) {
                return bundle;
            }

            Position outputPos = DispenserBlock.getOutputLocation(dispenserPointer);

            ItemStack dropStack = bundleBuilder.removeSelected();

            // If dropped any, play drop sound
            if (dropStack != null) {
                world.playSound(
                        null,
                        dispenserPos,
                        SoundEvents.ITEM_BUNDLE_DROP_CONTENTS,
                        SoundCategory.BLOCKS,
                        1.0F,
                        1.0F
                );
            }

            while (dropStack != null) {
                spawnItem(dispenserPointer.world(), dropStack, 6, direction, outputPos);
                dropStack = bundleBuilder.removeSelected();
            }
        }

        // Update bundle contents
        bundle.set(DataComponentTypes.BUNDLE_CONTENTS, bundleBuilder.build());
        return bundle; // Return updated bundle
    }
}