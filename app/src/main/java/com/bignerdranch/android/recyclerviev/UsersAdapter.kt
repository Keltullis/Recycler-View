package com.bignerdranch.android.recyclerviev

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.CustomPopupMenu
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.recyclerviev.databinding.ItemUserBinding
import com.bignerdranch.android.recyclerviev.model.User
import com.bumptech.glide.Glide

interface UserActionListener{
    fun onUserMove(user: User, moveBy:Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)
}

// Создаём DiffCallback,он определяет изменился ли объект
class UserDiffCallback(private val oldList:List<User>, private val newList: List<User>):DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        // Сравниваем id шники
        return oldUser.id == newUser.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        // Сравниваем контент
        return oldUser == newUser
    }

}

class UsersAdapter(private val actionListener: UserActionListener):RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(),View.OnClickListener {

    // Вызываем дифф колбэк,отправляем в него старый и новый список
    // вычисляем изменения и передаём обновление


    //private val usersService: UserService = UserService()
    var users:List<User> = emptyList()
        set(newValue) {
            val diffCallback = UserDiffCallback(field,newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            // Передаём обновление адаптеру(this)
            diffResult.dispatchUpdatesTo(this)
        }

    class UsersViewHolder(val binding:ItemUserBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        //users = usersService.getUsers()
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater,parent,false)

        // this это наш onClick
        binding.root.setOnClickListener(this)
        binding.moreImageViwButton.setOnClickListener(this)


        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding){
            holder.itemView.tag = user
            moreImageViwButton.tag = user

            userNameTextView.text = user.name
            userCompanyTextView.text = user.company

            if(user.photo.isNotBlank()){
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImageView)
            } else{
                Glide.with(photoImageView.context).clear(photoImageView)
                photoImageView.setImageResource(R.drawable.ic_user_avatar)
            }
        }
    }

    override fun getItemCount(): Int = users.size

    override fun onClick(view: View) {
        val user = view.tag as User
        when(view.id){
            R.id.moreImageViwButton -> {
                showPopupMenu(view)
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }

    }

    // Создаём попап меню
    private fun showPopupMenu(view: View) {
        // Добавляю иконки в PopupMenu,для этого нужно создать пакет androidx.appcompat.widget
        // Там создать класс CustomPopupMenu,отнаследоваться от PopupMenu и переопределить mPopup.setForceShowIcon(true)
        //val popupMenu = PopupMenu(view.context, view)
        val popupMenu = CustomPopupMenu(view.context,view)
        val context = view.context
        val user = view.tag as User
        val position = users.indexOfFirst { it.id == user.id }
        //1-гроуп айди ,2-айди действия,3-Порядок,4-название
        popupMenu.menu.add(0, ID_MOVE_UP,Menu.NONE,context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
            setIcon(R.drawable.ic_up)
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN,Menu.NONE,context.getString(R.string.move_down)).apply {
            isEnabled = position < users.size - 1
            setIcon(R.drawable.ic_down)
        }
        popupMenu.menu.add(0, ID_REMOVE,Menu.NONE,context.getString(R.string.remove)).apply {
            setIcon(R.drawable.ic_delete)
        }

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                ID_MOVE_UP -> {
                    actionListener.onUserMove(user,-1)
                }
                ID_MOVE_DOWN ->{
                    actionListener.onUserMove(user,1)
                }
                ID_REMOVE ->{
                    actionListener.onUserDelete(user)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    companion object{
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }
}