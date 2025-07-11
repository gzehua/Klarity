package io.github.numq.klarity.queue

import kotlinx.coroutines.flow.StateFlow

/**
 * MediaQueue provides a functionality for managing a queue of media items.
 * It supports operations such as shuffling, repeating, selecting, adding, and removing items,
 * as well as navigating between items in the queue.
 *
 * @param <Item> The type of the media items in the queue.
 * @param <SelectedItem> The type of the selected item.
 */
interface MediaQueue<Item, SelectedItem : Item> {
    /**
     * A StateFlow containing the current list of media items in the queue.
     */
    val items: StateFlow<List<Item>>

    /**
     * A StateFlow that represents whether the queue is shuffled.
     * `true` if the queue is shuffled, `false` otherwise.
     */
    val isShuffled: StateFlow<Boolean>

    /**
     * A StateFlow that represents the current repeat mode.
     * Possible values are `NONE`, `CIRCULAR`, and `SINGLE`.
     */
    val repeatMode: StateFlow<RepeatMode>

    /**
     * A StateFlow that represents the currently selected item in the queue.
     * It can be `Absent` if no item is selected, or `Present` with the selected item.
     */
    val selection: StateFlow<MediaQueueSelection<SelectedItem>>

    /**
     * A StateFlow that indicates whether there is a previous item to navigate to in the queue.
     * `true` if there is a previous item, `false` otherwise.
     */
    val hasPrevious: StateFlow<Boolean>

    /**
     * A StateFlow that indicates whether there is a next item to navigate to in the queue.
     * `true` if there is a next item, `false` otherwise.
     */
    val hasNext: StateFlow<Boolean>

    /**
     * Enables or disables shuffling of the queue. If enabled, the queue will be randomized based on a new seed;
     * if disabled, it resets to its original order.
     *
     * @param enabled whether shuffling should be enabled
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun setShuffleEnabled(enabled: Boolean): Result<Unit>

    /**
     * Sets the repeat mode for the queue.
     *
     * @param repeatMode the desired repeat mode (NONE, CIRCULAR, SINGLE)
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun setRepeatMode(repeatMode: RepeatMode): Result<Unit>

    /**
     * Selects the previous item in the queue if available. The behavior depends on the current repeat mode.
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun previous(): Result<Unit>

    /**
     * Selects the next item in the queue if available. The behavior depends on the current repeat mode.
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun next(): Result<Unit>

    /**
     * Selects a specific item in the queue. If the item is null or not in the queue, the selection is reset to `Absent`.
     *
     * @param item The item to select, or null to reset the selection
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun select(item: Item?): Result<Unit>

    /**
     * Adds a new item to the queue.
     *
     * @param item the item to add to the queue
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun add(item: Item): Result<Unit>

    /**
     * Removes an item from the queue. If the removed item is currently selected, the selection will move to the next or previous item if available.
     *
     * @param item the item to remove from the queue
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun delete(item: Item): Result<Unit>

    /**
     * Replaces an existing item in the queue with a new one. If the replaced item is currently selected, the selection moves to the new item.
     *
     * @param from the item to be replaced
     * @param to the new item to replace the existing one
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun replace(from: Item, to: Item): Result<Unit>

    /**
     * Clears the queue.
     *
     * @return [Result] indicating success or failure of the operation
     */
    suspend fun clear(): Result<Unit>

    companion object {
        /**
         * Creates a new instance of the default MediaQueue implementation.
         *
         * @param <Item> the type of the media items in the queue
         *
         * @return [Result] containing a [MediaQueue] instance
         */
        fun <Item, SelectedItem : Item> create(): Result<MediaQueue<Item, SelectedItem>> = runCatching {
            DefaultMediaQueue()
        }
    }
}