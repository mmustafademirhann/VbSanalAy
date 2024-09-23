package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.databinding.PostForRecyclerBinding
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.presentation.OnItemClickListener

import javax.inject.Inject

class PostAdapter @Inject constructor() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {


    private var posts: List<Post> = emptyList()
    private val mockItem = Post("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/" +
            "2wCEAAkGBxISEhUQEhIWEhUWFRUVFhYVFhUQFRUWFRUWFhURFRYYHSggGB0lGxMVITEhJSkrLi4uFx8zODMtNygtL" +
            "isBCgoKDg0OGhAQGysfICYrKy4tKystLSsrLS4rLi0tLSstLS0tKy0tKy0tLS0tLS0tLS0tLS0tLS0tLS0tKy0tLf/" +
            "AABEIAOEA4QMBIgACEQEDEQH/xAAcAAEAAQUBAQAAAAAAAAAAAAAABQEDBAYHAgj/xABLEAACAQIDBAUIBQcLAwUAAA" +
            "ABAgADEQQSIQUGMUETIlFhkWJxgZKhscHRBxUyQlIUI1RygpPCFiQzNHODstLh4vFTovAXJTVjo//EABoBAQADAQEBAAAAA" +
            "AAAAAAAAAABAwQFAgb/xAAqEQACAgEDBAICAQUBAAAAAAAAAQIDEQQSIQUTMVFBYSIycRQzgaGxwf/aAAwDAQACEQMRAD8A4" +
            "bERAEREAREQBKiUlRAOhYTcWjUw4ZXbpCA2a9xcjhbsv6e+aNtDBVKLmlUXKw8CORB5iT+5+9LYZhTqEmkfTkvzHd3Tet4NiUsd" +
            "SDKRmtdHGvu4ich6m3TW4u5i/D9FW5xfJx0xNkwO5GMqMVyBADbMx0PHVbcR85t+yfotBtnNSqbahFIHHjpr4mbLNbTD5y/S5PbmjlkT" +
            "v2D+i9FH9VT+8yt/ivM5NxMgstOio7AVUeAEzy6l6rl/w89z6PnOJ9BYvcwEWahTcDX7j+wyDx+5OGOjYfo9LaL0fhbSeV1WC/aLR57vtHGY" +
            "nSNo/RupF6FQqexusPHiPbMbYG4bLWvicpVdVUah+835d3/h0LqOncHJS/x8nruRwYe5+5hxA6avdaf3V4F/KPYP/PPD73bJTDV8lNiVIvYm5XU6e" +
            "adA3t3nTCp0dOxqEaKOXLMe73zlGKxDVGLuSzMbkmVaOd903bPiPwhBybyy1EROkWCIiAIiIAiIgCIiAIiIAiIgCIiAIlZO7q7tvi35rTB6zdvkr39/Ke" +
            "LJxri5SeEQ3gxdhbDrYt8lJdB9pj9lfme6dx3K3HanSFPMzDiWYkKOFwo7O7vkzulutSo01JXIg4KNC3eZtxqchoOQGk491r1H7cR9fLKnmXnwYmE2NQpD7" +
            "PSHv+yPR/zMt65AsLKOwC08lpbczzlRWIrH8HrheCzWYniSZh1RMqoZiVTM82eWYdWYzue0zJqmYlQzPJlbLeQHlbzaeye/ycEWIBE8rMinMsytmk72fR7SxF6" +
            "tL83V7dbN2Zh8ZyLbGyauGc06q5TrY65WtzU8+In0uZAby7EpYqmUqLfmDwIPaDynR0XUp14jPmP+0WQsa4Z88xJjePYVTCVMraoT1W7e498h59JCanFSjyjSnkRE" +
            "T0SIiIAiIgCIiAIiIAiIgCIiASWwdktiaoprcDizWvlH+vCd93O2FTpovVARLAC32iOZ7fiZp+4GwOipKSOvUOZiRqBbh6B8Z02gQAFGgAsJwNXqO9bhfqv9sok" +
            "8slBUvLoaYVN5e6SwueUrUicl5qgGpNvPPDPznGqu0BtCpVr4qo7U87LQoK7U0RFNgWykZmNrm8xdhbfOBxlNKTscPUdab0mYuq5yAHTMeqQSPReb3opbM55Pe07PUaY" +
            "tVpcqNMWq05UpFbLNVpi1DLtRpjO0okytnpJkUmHC+vZNN3x2m6GjhqbmmaxYvUHFaaWzBewnNa81/a2Bw1NQ9FqiVBr0gq1C5PaSWtNmn6bK+G9vHo9Rq3LJ1eYmJkPuFt" +
            "t8Xhc1TWpTc0nbQZiArB9O0MPSDJfEzmzrdc3CXlFTWHg1XeXZiYhCji/MHmDrYjv1nGtoYJ6NQ034jwI5ETueOmg767Mzr0qi7p2cSt9R32vfxnY6dqNj2Pwy6uWODQYlTKT" +
            "vGgREQBERAEREAREQBERAEmt0dndPiqaEXUHM3mXt9NpDToX0S4MFqtY2uLINNRzOvfp4TPq7e3TKX0eZPCOm7Pp5dfR85KUnkbTNhMmk8+Xg8IzolaTy+GmBSeZCvLVInJxPer" +
            "dTG4Oq/Q03rUGZmpugNSwY3yOq6gi9uFjxHdnbsbo1MRiaOJahVw+HphHbptGq1UN8qKdbEgEm1tO+dceuBzt6bS09W/O/tm6XUp7NuOfZZ3OCtR5jVHlXeY1R5y3IrbKVGmO7TL2BW" +
            "oVcS2FrZlcqHpG9lqDgwHlA8u+bLi916JHVLKfETZDp9tkN8Wh221k5XvpsGpikR6H9NSLFVuBnVrZkBP3uqCPSOc0fAbNxTu9Grg8Q7FCEAVqYWpcWZmawtx52+HZsbgHotlbUHgRwMuUXNr" +
            "XPjJq6hbpY9qcfAVjhw0RG5ewDgsKKLENUZjUqEG652AGVT2AKovz1MksTNe2/tHF0HZrEUr9V1sygeWLXXXnqPNLOD3qDHLVsL/eHxHxlc9JdcncmpZ5eCHBv8jKx017HC9wZsGMYEXBuDwI1k" +
            "BjZXVwQjlu08L0VVk7CbeY6j2TFmwb30bVEf8Skeqfkwmvz6ime+CkaovKEREsJEREAREQBESsApE2nd/cuviFFRgUQ8B94jt14embB/wCm6c3Yftqf4Jks11EHhs8OyK4Obzrn0V0LYQt+J2PnANtf" +
            "AzWNp7hOovSbNYcGIJPmIA7+U6Hu5sxKeDoDJkbokzgEjrEXYm3O95h6jqq7KMQecs8TkmuCSaqBLtLECXsFsik6BmBvc/eYcD556qbJoqrOEchftEM5A89py4aeySyjwot+DM2XReuxSnbSxYseAPO3Ey" +
            "9tEfk9Q06h0tmVuAYcD6QfhIfdSm74tK2FAKrdarFmK9G3FO9rgEDunRqtNGILKGK6qSASvaRfhOrToI2U88S9lnawsPya9uioqtXZlzLdAuZbjg18t/RIbauIFPE10bqjOMtxlFiinq9" +
            "ut5t2K3hwtM5amJpIewut/CXsPtChXBCVKdYcwGWp4iardCp0qrOMfOD04cYNV2ZsmpiKTVkIXW1MNezgcSTyF9B5pEF/zgpMcrdKtJtQQrM4Q6jsJnQMeKi0HTDBVqBCKQbqoGt1" +
            "b24Ccao4WsjGi+UVlNwgLu5qA5gbW16wBuON5jv0EIbNqz7+x2m/BM7bwLBshvTq0zmpv2HkwPNTOgbF2g1bD06rizlesO8cbTWcftEY1KVJALkB6hYdakNQ1Pua4Nx3S" +
            "U2htUU1CpYBbA5uqoAA6mY9XMbiwJF+U3aXTOmUknmPx9CMdrKbzaqp5hvYQb/CRNCX8Vilq4NsSQVzCwD6ZRmty4Xt55raYtB95fWecvqtDnanFfBE6Jze" +
            "YonNp4lKdGpUqWKIjM19bgDhOGVNo3JIFgSSAOABN8o83CdlWuiUhXZRULnLQp3LCq/aQdMg4knTQ9k07+RDY11q0mC0mLdJUAADMD1jSH3Uu" +
            "SF7heX6CC0tTnbxk9U0yTx8msbO3kq0tPtp+E/AzZMGxxQzUEd76WCkkHsNtJE7xbrYjCVQpomtTY5UKaXb7oPZ3zqW42xG" +
            "weGFNjd2JqVLcMzW6o7gAB6JT1HUaeMI2R5bfx/6Xw0jnJp8YND383Fq0tnDGOR0iVAWQa5KTjLxt9rNlvra3" +
            "bOTmfVG9tQNgMWraKcNWB526htbvvacC2TuPWqqGqHogeA4t3XmrRa2p05k8YPNtaq4NTidJX6N0PC" +
            "o/rL/AJJD7Y3Ar0lLoekA1twa3O1tD5pphr6JPG4pVkWadE9MJ5mw9iIiAJK7tYRatdVbg" +
            "Bmt22tYGRUkt3sRkxCH8RyG3laD22ldudksecES8He8LSCoqjhYf8zGxLzIwz" +
            "3RT5I90wMe1jPj0+TCWOl1khhny6ciLjuPMemQbVNZum6jjKy87KT" +
            "6b29kthXvlg1UVdwisTvCtCnkAu+ptz1PMcvT4ST3I3xo" +
            "VAuFcGjWJP2jmWsx4lWsNfJPtk/YdkqABra06+n" +
            "n2fBv7MUsIkaYp0gcoWmtyzWAUX4sxtO" +
            "ebz7x1K91UtTo8Aqkhqne5HLy" +
            "eHbeboHPafEzxpNf" +
            "9avhHjs/" +
            "ZycA8lC+33T1RY" +
            "qwaxQjg6Eqw77jUTqxAlbCV" +
            "LWWZyyx1Qx4I3dTeNqlqFY3e3UqcM+l8rDhmtz5" +
            "zYKz0qebENkSy9eoQAQi3OrcbDWYKypc9p8TLXrV6K+x9mk7N3ioV" +
            "" +
            "nqFLUmLubEZAesesAbcePfeVrUmquE4U7Xc6K9XXWm2RhnWx0utxlm5lR2CVUCR/W/Q7P2R" +
            "2LdcRQeitwSMt8rFQw4Xbhe4nJNv4ipTdqFihXRjYgnS9h3WPGdtzd58TPLgHjr59ZXLUQlJScfB7UJJYTOA7JxdZ6n5O" +
            "C3RlCGvc9Gl7mnTP3AxsCBxF+0zpv0e7VBSpg2GVqRLJyzU3J4eY+8TcQo7BKqBxmbVtaiO18ej3XHYRu1No0aKh6zhFLAC4vdtToAOOkjjvbghr04PmVj8JsTa8dfPrGRewTmLplTX5ttl7tfwc92hvR+Wv+TU" +
            "FIpKQ1R20L2NwgHIXHp7p4eprJfbKqKtV1ABNgbc8o/1E1w1NZE4xjLZFYSOTqpSc3uJfDPJDKCLHgZD4JrmS4OkqfkxM4nv3glp4litrNckDtBsT6dPbNbmwb8Vg2LfyQAfa3uYTX59bps9qOfSNsP1QiIl56Eu4" +
            "V7OrA2IZTfsseMtSogH0Bsitemn6o+Us7V0kXubjekw9JvJsfOCQR4yX2suk+MtWy1xfsxYwyGQZmA7SB4m02rdTEfnqg5MGt+wUA9hM1nBaMW/CrN6bWX2kSW3ZfLUpntZ18UE1U8cnU0UPwbN4zT1mljNKlppy" +
            "asF1WgNLStKBo3DBeZpXNpLDNKlo3DBeVpTNLStKZoyMF/NpKK0tFoVoyMF0NDNLOaC0jcMF/NpCtLObSA0nIwXc0qGljNPGIrZUZuxSfARkYNMxmIzNUPbdx5ukcH2FfCQ5Osz/vUgfvUsp/bv8SJHkTJavyyYd" +
            "dHDTJLZepkpialhI7Y6xtvFBEdibAKx7ORmVflZhHPaOL7wVc2Iqte/WI8NB7pHy5WfMxbhck+JvLc+zitsUjYvAiInokSolJWAdF+jzGfmcn4WI8Tm+Jm+4sXSch3GxeWsUvoy358VPyJ8BOv0Tmpz5Xqtey/Pvk" +
            "zWLEiHQWRj+Jgo8y9Y+9Zl4F8opN2Vh7QJZxYtlXsF/Sxv7rS/Tp3o+ap/DJr8I7dENtSRut5UtNZ+uK/k+qfnH1zX8j1T85byetpsymUvNa+ua/keqfnH1xX8j1T845J2mysZXNpNZ+uK/keqfnH1xX8j1T845I2" +
            "mzBpTNNa+uK/keqfnH1xX8n1T845G02bNpCmaz9c1/I9U/OPrmv5Hqn5xyNpst4YzWvriv5Hqn5x9c1/I9U/OOScGzZtIVprJ2xX8j1T84+uK/keqfnHJG02XNMLbtXLh6h8kD1iF+Mhxtiv5Hqn5yxjMfVqpkbLb" +
            "Q6Cx09ME4MPGLbou6mnumJj1tUPfZh+0L/EyT2mnWUdiKJhYxLhG86n0G49/slFjM+thmrPoz9mLZbzWd+MZloVdeKlfW0t4Xm10RZJzH6RMZcpTHMljx4DQefUnwnjp1fc1CORFZkjSpSVlJ9cahERAEREAyMFiT" +
            "Tdag+6Qey/aPCdy2DXFSmLG4I08x5zgs6Z9Ge1symix1TQfqnh8pyOr0b6lNfH/AArsXhmx4qoC7NyubeYaD2ASU2Suakbfj+EwNrU9ZJbtD8236/wE5NVm4206t2T24wi+MJ3QMJ3SQAhRNBsI44Tunk0lvbMt+y" +
            "4v5rTNxlUU1ZzyHt5DxmpBWt0vlWv5X2oJRsYwndBwndMvBVs6K45j28x4y6RAItkUaZl8RAVPxL6wkO1DPXKXtmdhe17anlJH+Tv/ANv/AGf7pIMmnQB4WPmsZU4TukVjNmPRHSK1xcardWF+Fx2emS+ycWaqXP2" +
            "gbHv0uDIB4NFb2uL8LXF79lp6GE7pEgfzv++/imykgC5NgOJMAj3w1uOnsnnolOgKnzEGR20Mc1dhTpg5b2A5se090l9m7MWktzYueJ5DyV7u/nAPIwvdKDCd0kQJ5UQCC204FQAm3VHxmPTXMpHYQw9Gh9jT1vKP" +
            "zo/UHvMxdlr1plsljJz7tX+1bXBJ4x8tP0Th+8ON6XEO4NxfKvmGnvufTOn/AEg7V6GgVH2m6q+nifQLzj5nT6LRiDsfz4MdS+RKRE7hcIiIAiIgCSGw9oNh6yVVvoQCBzBOokfKgyJRUk0wdyqYla1Jaim4IvJXd" +
            "n+ib9f4Ccr3e28VFgb3tmThYniVnUd0KmegXAsGYkX7LCfMy0sqJtfHwe9PU4Wp/BNgSiiXAJ5JABJ0ABJ8wknTIHebEcKf7R9wHvmV9W/zborda2b9vj/pIGriw1bpWGYZr5b2uBwX2CSx3kH/AEv+/wD2z0Tg87" +
            "tYj7VI/rD+Ie72ydYTT6eLy1ulUZRmzZb30J1W/jNxBBsRqCLj08JDIZqlBgMVcmw6RuOnbNlGJp/jX1hNXbD9JiDTvlzVGF7XtqeVxfhJL+TI/wCt/wDl/vkks97Z2gnRlFYMzaaa2HMnwlN2qRCM3IsAO/KNT7Z" +
            "cw+7tMHrOz+gUx7yfbJZaYFgAAALADQAdkgg1VmAxVybAViSewZuMptbaZqnKuiDhyLHtPylvG0i1d0HE1GA85a0mTsFeiyDWpxz8Ln8Pcv8AzJJL2ysElNAykMWFy3d2L2CZ5Gk1zYuPNNuiqaKTbXTI3Ye6bMwk" +
            "Mg8KJQCXFEookEGr7zj86P1B7zLOziFBY6Aaym+WLWnVGY/cHfzM0nbm8F0yfZXsvq+nA908f007pYRybanK1+vZA757Y/KcQWBui9Ve/mW9PwEgZV3JNzqTPM+mrrVcFCPhBJLhCIieyRERAEREASspEA9KxGon0" +
            "RuWv8xw3fRpn0lBrPnad3+iitm2bTFvsPVTz9cvf/v9kwdQi3BP7LqP3RtoEid4sTkp5BxfT0Dj8BJm0tvh0Y9ZFbzgH3zknQIbd7Ar0edlBLnS4Bso0HHtN/ZJZsKn4E9VflLoUDQCw7BoJ6Ik5IILeHAr0YdVAK" +
            "HWwAupsDe3fb2y5u5ic1PIeKG3oOo+I9EmMgOhAIOhB1B7jPCYdFPVRV8wC+6QSaphf63/AHr/AMU2wCUOGQHMEUHjfKL37by4BDGTwohp6UQYINRX+uf3/wDHNsUShwyXzZFzXvfKL37by4BBJAbybOveso1H2x2" +
            "j8fo5z1sDaWYdE56w+ye0dnnEnWEtfklMaimgI5hQCJIyegIUT2JQCQQcx+luoVYMNCET/E05VVqMxuxJPf4Tpf00V7VKac2QH0At8bTmM7WijirJzrM7n/JSIiaysREQBERAEREAREQBO5/Q/wD/ABw/tqnuWcMndPo" +
            "eH/tw/tqnuWZNd/a/yX6f9zdgIUT0BCqeycU3HgyrCVKypWAeVEGegvdBEAoZQCeipgL3QDyolDPYU9koVgFCJQCeysBYB5IlTKle6VKnsgHhRAE9KvdKBT2QDjv04f1nD/2J/wAbTm06V9OP9Zw/9if8bTms72m/tR" +
            "/g51v7sRES8rEREAREQBERAEREATNwu1a9NclOvVpre+VKjoLnibA2mFKwCS+v8X+lV/31T/NH1/i/0qv++qfORkrIwicsm9mbdxRrUg2JrkGogINWoQQWFwdZ2XZWHDHn4mcJ2T/T0v7Wn/iE75sOcTrHG3H2U2t8DF" +
            "4Rb8/Ey/srBKb3v4yuM4zI2RznA3y9lcWzn2/7NSzGm7IQCQVZlIPaCDOefX+L/Sq/76p/mnQvpL4P+qZyufVdN5p5L4N4JH6/xf6VX/fVPnOrbok1aFFnZmYoCWZixJ7SSdZxedn3GP8AN6H6glfVeKlj2ebW8Gy4zBL" +
            "bn4mWMFhFLjj4mSGN4CY+A+2J805y9lSbIrfTDhEJUkadpE49idu4sOyjFVwAxAAq1LAA2AGs7Nv5/RnzThGM/pH/AFm95n0HSOYPJdBszf5QYv8ASq/76p84+v8AF/pVf99U+cjZSdnaizLMjG46rVINWo9UgWBdmcgd" +
            "gLHSY8RJIEREAREQBERAEREAREQBERAEREAu0KpRlccVIYX4XBuL+E7hurtJXtY6HUemcKmwbt7wGgQrE5ORGpX5iYNfpXfDjyiuyOUdvxnGX9lMBcmaHR3qzC6urDz28QeEsYve9UU5qgHcvWY+gebjPnVobW8YKkmYX" +
            "0k44EsAeOnidfZOdGZ219pNXcudB90dg7++YE+p0tParUS+KwhOo7hbWU06a8CnUPnHA+E5dJDZO0moPmGo5jhfv88jV0d6vb8icco+hMUwIBHZLGB+2Jouz97lZQFqA2H2W6pHjx9Ev1N6cvWZ1Udt/ZPl5aG1SxgzpNMlf" +
            "pExyhSL8BrOJ1WuxPaSfEyf3p3h/KDlQkrzJ0La8hyE16fR6DTOmvEvJfFYKRETcexERAEREAREQBERAEREAREQBERAEREASoiIAMqIiSCkSkQBKxEgFWlBKRAYiIgCIiAIiIAiIgCIiAIiIB//2Q==",
        "Mock User")


    class PostViewHolder(private val binding:PostForRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            // Bind views using the View Binding
            binding.postUsername.text = post.username
            binding.postContent.text = "Nasıl ?"

            // Load image using Glide
            Glide.with(binding.root)
                .load(post.imageResId) // Assuming `post.imageUrl` holds the URL of the image
                .into(binding.postImage)
        }

    }





    fun setPosts(newPosts: List<Post>) {

        posts = newPosts +mockItem
        notifyDataSetChanged()
    }
    companion object {
        fun from(parent: ViewGroup): PostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PostForRecyclerBinding.inflate(layoutInflater, parent, false)
            return PostViewHolder(binding)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            PostForRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }
}