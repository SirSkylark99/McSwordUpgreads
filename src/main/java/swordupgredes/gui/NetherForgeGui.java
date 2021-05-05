
package swordupgredes.gui;

import swordupgredes.SwordupgreadesModElements;

import swordupgredes.SwordupgreadesMod;

import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Container;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

@SwordupgreadesModElements.ModElement.Tag
public class NetherForgeGui extends SwordupgreadesModElements.ModElement {
	public static HashMap guistate = new HashMap();
	private static ContainerType<GuiContainerMod> containerType = null;
	public NetherForgeGui(SwordupgreadesModElements instance) {
		super(instance, 1);
		elements.addNetworkMessage(ButtonPressedMessage.class, ButtonPressedMessage::buffer, ButtonPressedMessage::new,
				ButtonPressedMessage::handler);
		elements.addNetworkMessage(GUISlotChangedMessage.class, GUISlotChangedMessage::buffer, GUISlotChangedMessage::new,
				GUISlotChangedMessage::handler);
		containerType = new ContainerType<>(new GuiContainerModFactory());
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@OnlyIn(Dist.CLIENT)
	public void initElements() {
		DeferredWorkQueue.runLater(() -> ScreenManager.registerFactory(containerType, GuiWindow::new));
	}

	@SubscribeEvent
	public void registerContainer(RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(containerType.setRegistryName("nether_forge"));
	}
	public static class GuiContainerModFactory implements IContainerFactory {
		public GuiContainerMod create(int id, PlayerInventory inv, PacketBuffer extraData) {
			return new GuiContainerMod(id, inv, extraData);
		}
	}

	public static class GuiContainerMod extends Container implements Supplier<Map<Integer, Slot>> {
		private World world;
		private PlayerEntity entity;
		private int x, y, z;
		private IItemHandler internal;
		private Map<Integer, Slot> customSlots = new HashMap<>();
		private boolean bound = false;
		public GuiContainerMod(int id, PlayerInventory inv, PacketBuffer extraData) {
			super(containerType, id);
			this.entity = inv.player;
			this.world = inv.player.world;
			this.internal = new ItemStackHandler(102);
			BlockPos pos = null;
			if (extraData != null) {
				pos = extraData.readBlockPos();
				this.x = pos.getX();
				this.y = pos.getY();
				this.z = pos.getZ();
			}
			if (pos != null) {
				if (extraData.readableBytes() == 1) { // bound to item
					byte hand = extraData.readByte();
					ItemStack itemstack;
					if (hand == 0)
						itemstack = this.entity.getHeldItemMainhand();
					else
						itemstack = this.entity.getHeldItemOffhand();
					itemstack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
						this.internal = capability;
						this.bound = true;
					});
				} else if (extraData.readableBytes() > 1) {
					extraData.readByte(); // drop padding
					Entity entity = world.getEntityByID(extraData.readVarInt());
					if (entity != null)
						entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
							this.internal = capability;
							this.bound = true;
						});
				} else { // might be bound to block
					TileEntity ent = inv.player != null ? inv.player.world.getTileEntity(pos) : null;
					if (ent != null) {
						ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
							this.internal = capability;
							this.bound = true;
						});
					}
				}
			}
			this.customSlots.put(0, this.addSlot(new SlotItemHandler(internal, 0, 8, 9) {
			}));
			this.customSlots.put(1, this.addSlot(new SlotItemHandler(internal, 1, 8, 27) {
			}));
			this.customSlots.put(2, this.addSlot(new SlotItemHandler(internal, 2, 8, 45) {
			}));
			this.customSlots.put(3, this.addSlot(new SlotItemHandler(internal, 3, 8, 63) {
			}));
			this.customSlots.put(4, this.addSlot(new SlotItemHandler(internal, 4, 8, 81) {
			}));
			this.customSlots.put(5, this.addSlot(new SlotItemHandler(internal, 5, 8, 99) {
			}));
			this.customSlots.put(6, this.addSlot(new SlotItemHandler(internal, 6, 8, 117) {
			}));
			this.customSlots.put(7, this.addSlot(new SlotItemHandler(internal, 7, 8, 135) {
			}));
			this.customSlots.put(8, this.addSlot(new SlotItemHandler(internal, 8, 8, 153) {
			}));
			this.customSlots.put(9, this.addSlot(new SlotItemHandler(internal, 9, 8, 171) {
			}));
			this.customSlots.put(10, this.addSlot(new SlotItemHandler(internal, 10, 26, 9) {
			}));
			this.customSlots.put(11, this.addSlot(new SlotItemHandler(internal, 11, 26, 27) {
			}));
			this.customSlots.put(12, this.addSlot(new SlotItemHandler(internal, 12, 26, 45) {
			}));
			this.customSlots.put(13, this.addSlot(new SlotItemHandler(internal, 13, 26, 63) {
			}));
			this.customSlots.put(14, this.addSlot(new SlotItemHandler(internal, 14, 26, 81) {
			}));
			this.customSlots.put(15, this.addSlot(new SlotItemHandler(internal, 15, 26, 99) {
			}));
			this.customSlots.put(16, this.addSlot(new SlotItemHandler(internal, 16, 26, 117) {
			}));
			this.customSlots.put(17, this.addSlot(new SlotItemHandler(internal, 17, 26, 135) {
			}));
			this.customSlots.put(18, this.addSlot(new SlotItemHandler(internal, 18, 26, 153) {
			}));
			this.customSlots.put(19, this.addSlot(new SlotItemHandler(internal, 19, 26, 171) {
			}));
			this.customSlots.put(20, this.addSlot(new SlotItemHandler(internal, 20, 44, 9) {
			}));
			this.customSlots.put(21, this.addSlot(new SlotItemHandler(internal, 21, 44, 27) {
			}));
			this.customSlots.put(22, this.addSlot(new SlotItemHandler(internal, 22, 44, 45) {
			}));
			this.customSlots.put(23, this.addSlot(new SlotItemHandler(internal, 23, 44, 63) {
			}));
			this.customSlots.put(24, this.addSlot(new SlotItemHandler(internal, 24, 44, 81) {
			}));
			this.customSlots.put(25, this.addSlot(new SlotItemHandler(internal, 25, 44, 99) {
			}));
			this.customSlots.put(26, this.addSlot(new SlotItemHandler(internal, 26, 44, 117) {
			}));
			this.customSlots.put(27, this.addSlot(new SlotItemHandler(internal, 27, 44, 135) {
			}));
			this.customSlots.put(28, this.addSlot(new SlotItemHandler(internal, 28, 44, 153) {
			}));
			this.customSlots.put(29, this.addSlot(new SlotItemHandler(internal, 29, 44, 171) {
			}));
			this.customSlots.put(30, this.addSlot(new SlotItemHandler(internal, 30, 62, 9) {
			}));
			this.customSlots.put(31, this.addSlot(new SlotItemHandler(internal, 31, 62, 27) {
			}));
			this.customSlots.put(32, this.addSlot(new SlotItemHandler(internal, 32, 62, 45) {
			}));
			this.customSlots.put(33, this.addSlot(new SlotItemHandler(internal, 33, 62, 63) {
			}));
			this.customSlots.put(34, this.addSlot(new SlotItemHandler(internal, 34, 62, 81) {
			}));
			this.customSlots.put(35, this.addSlot(new SlotItemHandler(internal, 35, 62, 99) {
			}));
			this.customSlots.put(36, this.addSlot(new SlotItemHandler(internal, 36, 62, 117) {
			}));
			this.customSlots.put(37, this.addSlot(new SlotItemHandler(internal, 37, 62, 135) {
			}));
			this.customSlots.put(38, this.addSlot(new SlotItemHandler(internal, 38, 62, 153) {
			}));
			this.customSlots.put(39, this.addSlot(new SlotItemHandler(internal, 39, 62, 171) {
			}));
			this.customSlots.put(40, this.addSlot(new SlotItemHandler(internal, 40, 80, 9) {
			}));
			this.customSlots.put(41, this.addSlot(new SlotItemHandler(internal, 41, 80, 27) {
			}));
			this.customSlots.put(42, this.addSlot(new SlotItemHandler(internal, 42, 80, 45) {
			}));
			this.customSlots.put(43, this.addSlot(new SlotItemHandler(internal, 43, 80, 63) {
			}));
			this.customSlots.put(44, this.addSlot(new SlotItemHandler(internal, 44, 80, 81) {
			}));
			this.customSlots.put(45, this.addSlot(new SlotItemHandler(internal, 45, 80, 99) {
			}));
			this.customSlots.put(46, this.addSlot(new SlotItemHandler(internal, 46, 80, 117) {
			}));
			this.customSlots.put(47, this.addSlot(new SlotItemHandler(internal, 47, 80, 135) {
			}));
			this.customSlots.put(48, this.addSlot(new SlotItemHandler(internal, 48, 80, 153) {
			}));
			this.customSlots.put(49, this.addSlot(new SlotItemHandler(internal, 49, 80, 171) {
			}));
			this.customSlots.put(50, this.addSlot(new SlotItemHandler(internal, 50, 98, 9) {
			}));
			this.customSlots.put(51, this.addSlot(new SlotItemHandler(internal, 51, 98, 27) {
			}));
			this.customSlots.put(52, this.addSlot(new SlotItemHandler(internal, 52, 98, 45) {
			}));
			this.customSlots.put(53, this.addSlot(new SlotItemHandler(internal, 53, 98, 63) {
			}));
			this.customSlots.put(54, this.addSlot(new SlotItemHandler(internal, 54, 98, 81) {
			}));
			this.customSlots.put(55, this.addSlot(new SlotItemHandler(internal, 55, 98, 99) {
			}));
			this.customSlots.put(56, this.addSlot(new SlotItemHandler(internal, 56, 98, 117) {
			}));
			this.customSlots.put(57, this.addSlot(new SlotItemHandler(internal, 57, 98, 135) {
			}));
			this.customSlots.put(58, this.addSlot(new SlotItemHandler(internal, 58, 98, 153) {
			}));
			this.customSlots.put(59, this.addSlot(new SlotItemHandler(internal, 59, 98, 171) {
			}));
			this.customSlots.put(60, this.addSlot(new SlotItemHandler(internal, 60, 116, 9) {
			}));
			this.customSlots.put(61, this.addSlot(new SlotItemHandler(internal, 61, 116, 27) {
			}));
			this.customSlots.put(62, this.addSlot(new SlotItemHandler(internal, 62, 116, 45) {
			}));
			this.customSlots.put(63, this.addSlot(new SlotItemHandler(internal, 63, 116, 63) {
			}));
			this.customSlots.put(64, this.addSlot(new SlotItemHandler(internal, 64, 116, 81) {
			}));
			this.customSlots.put(65, this.addSlot(new SlotItemHandler(internal, 65, 116, 99) {
			}));
			this.customSlots.put(66, this.addSlot(new SlotItemHandler(internal, 66, 116, 117) {
			}));
			this.customSlots.put(67, this.addSlot(new SlotItemHandler(internal, 67, 116, 135) {
			}));
			this.customSlots.put(68, this.addSlot(new SlotItemHandler(internal, 68, 116, 153) {
			}));
			this.customSlots.put(69, this.addSlot(new SlotItemHandler(internal, 69, 116, 171) {
			}));
			this.customSlots.put(70, this.addSlot(new SlotItemHandler(internal, 70, 134, 9) {
			}));
			this.customSlots.put(71, this.addSlot(new SlotItemHandler(internal, 71, 134, 27) {
			}));
			this.customSlots.put(72, this.addSlot(new SlotItemHandler(internal, 72, 134, 45) {
			}));
			this.customSlots.put(73, this.addSlot(new SlotItemHandler(internal, 73, 134, 63) {
			}));
			this.customSlots.put(74, this.addSlot(new SlotItemHandler(internal, 74, 134, 81) {
			}));
			this.customSlots.put(75, this.addSlot(new SlotItemHandler(internal, 75, 134, 99) {
			}));
			this.customSlots.put(76, this.addSlot(new SlotItemHandler(internal, 76, 134, 117) {
			}));
			this.customSlots.put(77, this.addSlot(new SlotItemHandler(internal, 77, 134, 135) {
			}));
			this.customSlots.put(78, this.addSlot(new SlotItemHandler(internal, 78, 134, 153) {
			}));
			this.customSlots.put(79, this.addSlot(new SlotItemHandler(internal, 79, 134, 171) {
			}));
			this.customSlots.put(80, this.addSlot(new SlotItemHandler(internal, 80, 152, 9) {
			}));
			this.customSlots.put(81, this.addSlot(new SlotItemHandler(internal, 81, 152, 27) {
			}));
			this.customSlots.put(82, this.addSlot(new SlotItemHandler(internal, 82, 152, 45) {
			}));
			this.customSlots.put(83, this.addSlot(new SlotItemHandler(internal, 83, 152, 63) {
			}));
			this.customSlots.put(84, this.addSlot(new SlotItemHandler(internal, 84, 152, 81) {
			}));
			this.customSlots.put(85, this.addSlot(new SlotItemHandler(internal, 85, 152, 99) {
			}));
			this.customSlots.put(86, this.addSlot(new SlotItemHandler(internal, 86, 152, 117) {
			}));
			this.customSlots.put(87, this.addSlot(new SlotItemHandler(internal, 87, 152, 135) {
			}));
			this.customSlots.put(88, this.addSlot(new SlotItemHandler(internal, 88, 152, 153) {
			}));
			this.customSlots.put(89, this.addSlot(new SlotItemHandler(internal, 89, 152, 171) {
			}));
			this.customSlots.put(90, this.addSlot(new SlotItemHandler(internal, 90, 170, 9) {
			}));
			this.customSlots.put(91, this.addSlot(new SlotItemHandler(internal, 91, 170, 27) {
			}));
			this.customSlots.put(92, this.addSlot(new SlotItemHandler(internal, 92, 170, 45) {
			}));
			this.customSlots.put(93, this.addSlot(new SlotItemHandler(internal, 93, 170, 63) {
			}));
			this.customSlots.put(94, this.addSlot(new SlotItemHandler(internal, 94, 170, 81) {
			}));
			this.customSlots.put(95, this.addSlot(new SlotItemHandler(internal, 95, 170, 99) {
			}));
			this.customSlots.put(96, this.addSlot(new SlotItemHandler(internal, 96, 170, 117) {
			}));
			this.customSlots.put(97, this.addSlot(new SlotItemHandler(internal, 97, 170, 135) {
			}));
			this.customSlots.put(98, this.addSlot(new SlotItemHandler(internal, 98, 170, 153) {
			}));
			this.customSlots.put(99, this.addSlot(new SlotItemHandler(internal, 99, 170, 171) {
			}));
			this.customSlots.put(100, this.addSlot(new SlotItemHandler(internal, 100, 80, 207) {
			}));
			this.customSlots.put(101, this.addSlot(new SlotItemHandler(internal, 101, 332, 90) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return false;
				}
			}));
			int si;
			int sj;
			for (si = 0; si < 3; ++si)
				for (sj = 0; sj < 9; ++sj)
					this.addSlot(new Slot(inv, sj + (si + 1) * 9, 257 + 8 + sj * 18, 79 + 84 + si * 18));
			for (si = 0; si < 9; ++si)
				this.addSlot(new Slot(inv, si, 257 + 8 + si * 18, 79 + 142));
		}

		public Map<Integer, Slot> get() {
			return customSlots;
		}

		@Override
		public boolean canInteractWith(PlayerEntity player) {
			return true;
		}

		@Override
		public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
			ItemStack itemstack = ItemStack.EMPTY;
			Slot slot = (Slot) this.inventorySlots.get(index);
			if (slot != null && slot.getHasStack()) {
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();
				if (index < 102) {
					if (!this.mergeItemStack(itemstack1, 102, this.inventorySlots.size(), true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				} else if (!this.mergeItemStack(itemstack1, 0, 102, false)) {
					if (index < 102 + 27) {
						if (!this.mergeItemStack(itemstack1, 102 + 27, this.inventorySlots.size(), true)) {
							return ItemStack.EMPTY;
						}
					} else {
						if (!this.mergeItemStack(itemstack1, 102, 102 + 27, false)) {
							return ItemStack.EMPTY;
						}
					}
					return ItemStack.EMPTY;
				}
				if (itemstack1.getCount() == 0) {
					slot.putStack(ItemStack.EMPTY);
				} else {
					slot.onSlotChanged();
				}
				if (itemstack1.getCount() == itemstack.getCount()) {
					return ItemStack.EMPTY;
				}
				slot.onTake(playerIn, itemstack1);
			}
			return itemstack;
		}

		@Override /**
					 * Merges provided ItemStack with the first avaliable one in the
					 * container/player inventor between minIndex (included) and maxIndex
					 * (excluded). Args : stack, minIndex, maxIndex, negativDirection. /!\ the
					 * Container implementation do not check if the item is valid for the slot
					 */
		protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
			boolean flag = false;
			int i = startIndex;
			if (reverseDirection) {
				i = endIndex - 1;
			}
			if (stack.isStackable()) {
				while (!stack.isEmpty()) {
					if (reverseDirection) {
						if (i < startIndex) {
							break;
						}
					} else if (i >= endIndex) {
						break;
					}
					Slot slot = this.inventorySlots.get(i);
					ItemStack itemstack = slot.getStack();
					if (slot.isItemValid(itemstack) && !itemstack.isEmpty() && areItemsAndTagsEqual(stack, itemstack)) {
						int j = itemstack.getCount() + stack.getCount();
						int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
						if (j <= maxSize) {
							stack.setCount(0);
							itemstack.setCount(j);
							slot.putStack(itemstack);
							flag = true;
						} else if (itemstack.getCount() < maxSize) {
							stack.shrink(maxSize - itemstack.getCount());
							itemstack.setCount(maxSize);
							slot.putStack(itemstack);
							flag = true;
						}
					}
					if (reverseDirection) {
						--i;
					} else {
						++i;
					}
				}
			}
			if (!stack.isEmpty()) {
				if (reverseDirection) {
					i = endIndex - 1;
				} else {
					i = startIndex;
				}
				while (true) {
					if (reverseDirection) {
						if (i < startIndex) {
							break;
						}
					} else if (i >= endIndex) {
						break;
					}
					Slot slot1 = this.inventorySlots.get(i);
					ItemStack itemstack1 = slot1.getStack();
					if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
						if (stack.getCount() > slot1.getSlotStackLimit()) {
							slot1.putStack(stack.split(slot1.getSlotStackLimit()));
						} else {
							slot1.putStack(stack.split(stack.getCount()));
						}
						slot1.onSlotChanged();
						flag = true;
						break;
					}
					if (reverseDirection) {
						--i;
					} else {
						++i;
					}
				}
			}
			return flag;
		}

		@Override
		public void onContainerClosed(PlayerEntity playerIn) {
			super.onContainerClosed(playerIn);
			if (!bound && (playerIn instanceof ServerPlayerEntity)) {
				if (!playerIn.isAlive() || playerIn instanceof ServerPlayerEntity && ((ServerPlayerEntity) playerIn).hasDisconnected()) {
					for (int j = 0; j < internal.getSlots(); ++j) {
						playerIn.dropItem(internal.extractItem(j, internal.getStackInSlot(j).getCount(), false), false);
					}
				} else {
					for (int i = 0; i < internal.getSlots(); ++i) {
						playerIn.inventory.placeItemBackInInventory(playerIn.world,
								internal.extractItem(i, internal.getStackInSlot(i).getCount(), false));
					}
				}
			}
		}

		private void slotChanged(int slotid, int ctype, int meta) {
			if (this.world != null && this.world.isRemote) {
				SwordupgreadesMod.PACKET_HANDLER.sendToServer(new GUISlotChangedMessage(slotid, x, y, z, ctype, meta));
				handleSlotAction(entity, slotid, ctype, meta, x, y, z);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class GuiWindow extends ContainerScreen<GuiContainerMod> {
		private World world;
		private int x, y, z;
		private PlayerEntity entity;
		public GuiWindow(GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
			super(container, inventory, text);
			this.world = container.world;
			this.x = container.x;
			this.y = container.y;
			this.z = container.z;
			this.entity = container.entity;
			this.xSize = 429;
			this.ySize = 241;
		}
		private static final ResourceLocation texture = new ResourceLocation("swordupgreades:textures/nether_forge.png");
		@Override
		public void render(int mouseX, int mouseY, float partialTicks) {
			this.renderBackground();
			super.render(mouseX, mouseY, partialTicks);
			this.renderHoveredToolTip(mouseX, mouseY);
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int gx, int gy) {
			RenderSystem.color4f(1, 1, 1, 1);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			Minecraft.getInstance().getTextureManager().bindTexture(texture);
			int k = (this.width - this.xSize) / 2;
			int l = (this.height - this.ySize) / 2;
			this.blit(k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
			RenderSystem.disableBlend();
		}

		@Override
		public boolean keyPressed(int key, int b, int c) {
			if (key == 256) {
				this.minecraft.player.closeScreen();
				return true;
			}
			return super.keyPressed(key, b, c);
		}

		@Override
		public void tick() {
			super.tick();
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		}

		@Override
		public void removed() {
			super.removed();
			Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
		}

		@Override
		public void init(Minecraft minecraft, int width, int height) {
			super.init(minecraft, width, height);
			minecraft.keyboardListener.enableRepeatEvents(true);
			this.addButton(new Button(this.guiLeft + 241, this.guiTop + 89, 50, 20, "Forge", e -> {
				if (true) {
					SwordupgreadesMod.PACKET_HANDLER.sendToServer(new ButtonPressedMessage(0, x, y, z));
					handleButtonAction(entity, 0, x, y, z);
				}
			}));
		}
	}

	public static class ButtonPressedMessage {
		int buttonID, x, y, z;
		public ButtonPressedMessage(PacketBuffer buffer) {
			this.buttonID = buffer.readInt();
			this.x = buffer.readInt();
			this.y = buffer.readInt();
			this.z = buffer.readInt();
		}

		public ButtonPressedMessage(int buttonID, int x, int y, int z) {
			this.buttonID = buttonID;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public static void buffer(ButtonPressedMessage message, PacketBuffer buffer) {
			buffer.writeInt(message.buttonID);
			buffer.writeInt(message.x);
			buffer.writeInt(message.y);
			buffer.writeInt(message.z);
		}

		public static void handler(ButtonPressedMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				PlayerEntity entity = context.getSender();
				int buttonID = message.buttonID;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				handleButtonAction(entity, buttonID, x, y, z);
			});
			context.setPacketHandled(true);
		}
	}

	public static class GUISlotChangedMessage {
		int slotID, x, y, z, changeType, meta;
		public GUISlotChangedMessage(int slotID, int x, int y, int z, int changeType, int meta) {
			this.slotID = slotID;
			this.x = x;
			this.y = y;
			this.z = z;
			this.changeType = changeType;
			this.meta = meta;
		}

		public GUISlotChangedMessage(PacketBuffer buffer) {
			this.slotID = buffer.readInt();
			this.x = buffer.readInt();
			this.y = buffer.readInt();
			this.z = buffer.readInt();
			this.changeType = buffer.readInt();
			this.meta = buffer.readInt();
		}

		public static void buffer(GUISlotChangedMessage message, PacketBuffer buffer) {
			buffer.writeInt(message.slotID);
			buffer.writeInt(message.x);
			buffer.writeInt(message.y);
			buffer.writeInt(message.z);
			buffer.writeInt(message.changeType);
			buffer.writeInt(message.meta);
		}

		public static void handler(GUISlotChangedMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				PlayerEntity entity = context.getSender();
				int slotID = message.slotID;
				int changeType = message.changeType;
				int meta = message.meta;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				handleSlotAction(entity, slotID, changeType, meta, x, y, z);
			});
			context.setPacketHandled(true);
		}
	}
	private static void handleButtonAction(PlayerEntity entity, int buttonID, int x, int y, int z) {
		World world = entity.world;
		// security measure to prevent arbitrary chunk generation
		if (!world.isBlockLoaded(new BlockPos(x, y, z)))
			return;
	}

	private static void handleSlotAction(PlayerEntity entity, int slotID, int changeType, int meta, int x, int y, int z) {
		World world = entity.world;
		// security measure to prevent arbitrary chunk generation
		if (!world.isBlockLoaded(new BlockPos(x, y, z)))
			return;
	}
}
